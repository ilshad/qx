(ns qx.daemon
  (:require [io.pedestal.log :as log]))

(def running (atom #{}))

(def running? (partial contains? @running))

(defn- runnable [service id start]
  (fn []
    (try
      (start service (partial running? id))
      (catch InterruptedException e
        (log/debug ::interrupted id)))))

(defn run [service id start & [stop]]
  (swap! running conj id)
  (let [thread (Thread. (runnable service id start) (str id))]
    (.setDaemon thread true)
    (.start thread)
    (log/info ::started id)
    (assoc-in service [::stop id]
      (fn []
        (.interrupt thread)
        (when stop (stop service))
        (log/info ::stopped id)))))

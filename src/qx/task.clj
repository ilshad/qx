(ns qx.task
  (:require [io.pedestal.log :as log]
            [qx.db :as db]))

(defn topic-dispatcher [service task] (:task/topic task))

(defmulti handle-defer topic-dispatcher)
(defmulti handle-queue topic-dispatcher)

(defmethod handle-defer :default [service task]
  (db/put service (assoc task :task/status :task.status/queue)))

(defmethod handle-queue :default [service task]
  (throw (ex-info (str "Not found handle-queue for topic:" (:task/topic task))
                  {:task task :service service})))

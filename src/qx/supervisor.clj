(ns qx.supervisor
  (:require [qx.task :as task]
            [qx.db :as db]))

(defn- run* [service task]
  (try
    (or (task/handle-queue service task)
        (assoc task :task/status :task.status/success))
    (catch Exception e
      (assoc task
        :task/status :task.status/fail
        :task.fail/reason (str e)))))

(defn run [service task]
  (db/put service (assoc task :task/status :task.status/active))
  (db/put service (run* service task)))

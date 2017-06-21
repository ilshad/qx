(ns qx.supervisor
  (:require [qx.task :as task]))

(defn run [service task]
  (task/update-status service task :task.status/active)
  (try
    (task/handle-queue service task)
    (catch Exception e
      nil)))

(ns qx.queue
  (:require [datomic.api :as d]
            [io.pedestal.log :as log]))

(defn consume-queued-task [_ task]
  (log/info ::consume-queued-task$task task))

(defn collect-tasks [db status]
  (fn [acc [eid _ _ _ _]]
    (let [e (d/entity db eid)]
      (if (= (:task/status e) status)
        (conj acc (into {} e))
        acc))))

(defn start [service running?]
  (let [queue (d/tx-report-queue (:qx.db/conn service))]
    (while (running?)
      (when-let [{data :tx-data db :db-after} (.take queue)]
        (doseq [e (reduce (collect-tasks db :task.status/queue) #{} data)]
          (consume-queued-task service e))))))

(defn stop [service]
  (d/remove-tx-report-queue (:qx.db/conn service)))

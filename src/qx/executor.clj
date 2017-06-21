(ns qx.executor
  (:import [java.util.concurrent ThreadPoolExecutor
                                 PriorityBlockingQueue
                                 TimeUnit]))

(defn queue [policy]
  (case policy
    :fifo (PriorityBlockingQueue.)))

(defn run-task [task])

(defn create-executor [service [id {:keys [::pool ::policy]}]]
  (let [e (ThreadPoolExecutor. pool pool 0 TimeUnit/MILLISECONDS (queue policy))]
    (update-in service [:qx/executors id] assoc
      ::executor e
      ::fn (fn [task] (.execute e (run-task task))))))

(defn create-executors [service]
  (reduce create-executor service (:qx/executors service)))
(ns qx.executor
  (:require [io.pedestal.log :as log]
            [qx.supervisor :as supervisor])
  (:import [java.util.concurrent ThreadPoolExecutor
                                 PriorityBlockingQueue
                                 TimeUnit]))

(defn- queue [policy]
  (case policy
    :fifo (PriorityBlockingQueue.)))

(defn- add-executor [service [id {:keys [::pool ::policy]}]]
  (let [e (ThreadPoolExecutor. pool pool 0 TimeUnit/MILLISECONDS (queue policy))]
    (assoc-in service [:qx/executors id ::fn]
      (fn [task] (.execute e #(supervisor/run service task))))))

(defn create-executors [service]
  (reduce add-executor service (:qx/executors service)))

(defn topic [service task]
  (or (get-in service [:qx/topics (:task/topic task)])
      (throw (ex-info "Topic not found" {:task task}))))

(defn task-executor [service task]
  (let [k (:qx.topic/executor (topic service task))]
    (get-in service [:qx/executors k ::fn])))

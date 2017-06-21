(ns qx.executor
  (:require [io.pedestal.log :as log])
  (:import [java.util.concurrent ThreadPoolExecutor
                                 PriorityBlockingQueue
                                 TimeUnit]))

(defn- run-task [task]
  (fn []
    (log/debug ::run-task$task task)))

(defn- queue [policy]
  (case policy
    :fifo (PriorityBlockingQueue.)))

(defn- add-executor [service [id {:keys [::pool ::policy]}]]
  (let [e (ThreadPoolExecutor. pool pool 0 TimeUnit/MILLISECONDS (queue policy))]
    (assoc-in service [:qx/executors id ::execute]
      (fn [task] (.execute e (run-task task))))))

(defn create-executors [service]
  (reduce add-executor service (:qx/executors service)))

(defn task-executor [service task]
  (let [id (get-in service [:qx/topics (:task/topic task) :qx.topic/executor])]
    (get-in service [:qx/executors id ::fn])))

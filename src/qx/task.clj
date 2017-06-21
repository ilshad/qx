(ns qx.task
  (:require [datomic.api :as d]
            [io.pedestal.log :as log]))

(defn- update-status [service task status]
  (d/transact (:qx.db/conn service) [(assoc task :task/status status)]))

(defmulti handle-defer (fn [service task] (:task/topic task)))

(defmethod handle-defer :default [service task]
  (update-status service task :task.status/queue))

(defmulti handle-queue (fn [service task] (:task/topic task)))

(defmethod handle-queue :default [service task]
  (log/info ::handle-queue task))
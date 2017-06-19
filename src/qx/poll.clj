(ns qx.poll
  (:require [datomic.api :as d]
            [io.pedestal.log :as log])
  (:import [java.time LocalDate]))

(defn update-status [service task status]
  (d/transact (:qx.db/conn service) [(assoc task :task/status status)]))

(defmulti handle-defer
  (fn [service task] (:task/topic task)))

(defmethod handle-defer :default [service task]
  (update-status service task :task.status/queue))

(defn query-defer-by-date [^LocalDate date db]
  (map (partial d/entity db)
    (d/q '[:find [?e ...]
           :in $ ?date
           :where
           [?e :task.defer/date ?date]
           [?e :task/status :task.status/defer]]
      db (.toEpochDay date))))

(defn run-defer-by-date [service ^LocalDate date]
  (doseq [task (query-defer-by-date date (d/db (:qx.db/conn service)))]
    (handle-defer service (into {} task))))

(defn start [service running?]
  (while (running?)
    (run-defer-by-date service (LocalDate/now))
    (Thread/sleep (:qx/daily-polling-interval service))))

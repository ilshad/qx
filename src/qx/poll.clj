(ns qx.poll
  (:require [datomic.api :as d]
            [io.pedestal.log :as log]
            [qx.task :as task])
  (:import [java.time LocalDate]))

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
    (task/handle-defer service (into {} task))))

(defn start [service running?]
  (while (running?)
    (run-defer-by-date service (LocalDate/now))
    (Thread/sleep (:qx/daily-polling-interval service))))

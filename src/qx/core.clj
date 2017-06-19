(ns qx.core
  (:require [datomic.api :as d]
            [io.pedestal.log :as log]
            [qx.daemon :as daemon]
            [qx.queue :as queue]
            [qx.poll :as poll]
            [qx.spec :as spec]
            [qx.db :as db])
  (:import [java.time LocalDate]))

(defonce service (atom {}))

(defn start [config]
  (swap! service merge config)
  (swap! service db/start)
  (swap! service daemon/run :qx/poll poll/start)
  (swap! service daemon/run :qx/queue queue/start queue/stop)
  (log/info :started @service))

(defn stop []
  (doseq [[_ func] (::daemon/stop @service)]
    (func)))

(defn put [task]
  (d/transact (::db/conn @service) [(assoc task :task/id (d/squuid))]))

(defn put-defer [topic ^LocalDate date payload & args]
  (put (merge {:task/topic topic
               :task/status :task.status/defer
               :task.defer/date (.toEpochDay date)
               :task/payload payload}
              args)))

(defn put-queue [topic payload & args]
  (put (merge {:task/topic topic
               :task/status :task.status/queue
               :task/payload payload}
              args)))

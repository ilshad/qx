(ns qx.db
  (:require [datomic.api :as d]
            [qx.schema :as schema]))

(defn start [service]
  (let [uri (:qx/db-uri service)
        _ (d/create-database uri)
        conn (d/connect uri)]
    (d/transact conn schema/schema)
    (assoc service ::conn conn)))

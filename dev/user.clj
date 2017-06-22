(ns user
  (:require [datomic.api :as d]
            qx.core
            sample))

(defn start []
  (qx.core/start sample/config))

(defn stop []
  (qx.core/stop))

(defn all-tasks []
  (group-by #(-> % :task/status :db/ident)
            (d/q '[:find [(pull ?e [:task/id
                                    {:task/status [:db/ident]}
                                    :task/topic
                                    :task/payload
                                    :task.fail/reason]) ...]
                   :where [?e :task/id _]]
                 (d/db (:qx.db/conn @qx.core/service)))))

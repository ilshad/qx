(ns qx.schema)

(def schema
  [
   {:db/ident :task/id
    :db/valueType :db.type/uuid
    :db/cardinality :db.cardinality/one
    :db/unique :db.unique/identity}

   {:db/ident :task/topic
    :db/valueType :db.type/keyword
    :db/cardinality :db.cardinality/one}

   {:db/ident :task/priority
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident :task/status
    :db/valueType :db.type/ref
    :db/cardinality :db.cardinality/one}

   {:db/ident :task.status/defer}
   {:db/ident :task.status/queue}
   {:db/ident :task.status/active}
   {:db/ident :task.status/success}
   {:db/ident :task.status/fail}

   {:db/ident :task/payload
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}

   {:db/ident :task.defer/date
    :db/valueType :db.type/long
    :db/cardinality :db.cardinality/one}

   {:db/ident :task.fail/reason
    :db/valueType :db.type/string
    :db/cardinality :db.cardinality/one}
   ])

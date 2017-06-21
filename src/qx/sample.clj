(ns qx.sample)

(def config
  {:qx.db/uri "datomic:mem://qx"
   :qx/daily-polling-interval 60000
   :qx/executors {:executor-1 {:qx.executor/pool 10
                               :qx.executor/policy :fifo}}
   :qx/topics {:topic/test {:qx.topic/executor :executor-1}}})

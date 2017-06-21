(ns qx.sample)

(def config
  {:qx.db/uri "datomic:mem://qx"
   :qx/daily-polling-interval 60000
   :qx/pools {:pool-1 {:qx.pool/size 10}}
   :qx/topics {:topic/test {:qx.topic/policy :fifo
                            :qx.topic/priority? true
                            :qx.topic/pool :pool-1}}})

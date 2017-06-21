(ns qx.spec
  (:require [clojure.spec.alpha :as s]))

;; Config (service description)
(s/def :qx/db-uri string?)
(s/def :qx/daily-polling-interval integer?)

(s/def :qx/executors (s/map-of keyword? :qx/executor))
(s/def :qx/executor (s/keys :req [:qx.executor/pool
                                  :qx.executor/policy]
                            :opt [:qx.executor/fn]))

(s/def :qx.executor/pool integer?)
(s/def :qx.executor/policy #{:fifo :lifo})

(s/def :qx/topics (s/map-of keyword? :qx/topic))
(s/def :qx/topic (s/keys :req [:qx.topic/executor]))
(s/def :qx.topic/executor keyword?)

(s/def :qx/config
  (s/keys :req [:qx.db/uri
                :qx/daily-polling-interval]
          :opt [:qx/executors
                :qx/topics]))

;; Service map
(s/def :qx/service
  (s/and :qx/config
         (s/spec (s/keys :req [:qx.db/conn]))))

;; Task
(s/def :qx/task
  (s/keys :req [:task/id
                :task/topic
                :task/status
                :task/payload]
          :opt [:task/priority
                :task.defer/date
                :task.fail/reason]))

(s/def :task/id uuid?)
(s/def :task/topic keyword?)
(s/def :task/payload string?)
(s/def :task/priority integer?)
(s/def :task.defer/date integer?)
(s/def :task.fail/reason string?)
(s/def :task/status #{:task.status/defer
                      :task.status/queue
                      :task.status/active
                      :task.status/success
                      :task.status/fail})

(s/def :qx/result keyword?)

;; Internals
(s/def :qx.executor/fn fn?)
(s/def :qx.db/conn #(= (type %) datomic.peer.LocalConnection))

;; Fns
(s/fdef qx.supervisor/supervise
  :args (s/cat :service :qx/service
               :task :qx/task
               :result :qx/result))

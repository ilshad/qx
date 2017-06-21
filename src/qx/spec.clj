(ns qx.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :qx/db-uri string?)
(s/def :qx/daily-polling-interval integer?)

(s/def :qx/executors (s/map-of keyword? :qx/executor))
(s/def :qx/executor (s/keys :req [:qx.executor/pool
                                  :qx.executor/policy]
                            :opt [:qx.executor/fn]))

(s/def :qx.executor/pool integer?)
(s/def :qx.executor/policy #{:fifo :lifo})
(s/def :qx.executor/fn fn?)

(s/def :qx/topics (s/map-of keyword? :qx/topic))
(s/def :qx/topic (s/keys :req [:qx.topic/executor]))
(s/def :qx.topic/executor keyword?)

(s/def :qx/config
  (s/keys :req [:qx.db/uri
                :qx/daily-polling-interval]
          :opt [:qx/executors
                :qx/topics]))

(s/def :qx/service
  (s/and :qx/config
         (s/spec (s/keys :req [:qx.db/conn]))))

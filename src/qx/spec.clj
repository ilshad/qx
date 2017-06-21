(ns qx.spec
  (:require [clojure.spec.alpha :as s]))

(s/def :qx/daily-polling-interval integer?)
(s/def :qx/pools (s/map-of keyword? :qx/pool))
(s/def :qx/topics (s/map-of keyword? :qx/topic))

(s/def :qx/pool (s/keys :req [:qx.pool/size]))
(s/def :qx.pool/size integer?)

(s/def :qx/topic
  (s/keys :req [:qx.topic/policy
                :qx.topic/pool]))

(s/def :qx.topic/policy #{:fifo :lifo :priority})
(s/def :qx.topic/pool keyword?)

(s/def :qx/db-uri string?)

(s/def :qx/config
  (s/keys :req [:qx.db/uri
                :qx/daily-polling-interval]
          :opt [:qx/pools
                :qx/topics]))

(s/def :qx/service
  (s/and :qx/config
         (s/spec (s/keys :req [:qx.db/conn]))))

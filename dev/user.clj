(ns user
  (:require qx.core
            qx.sample))

(defn start []
  (qx.core/start qx.sample/config))

(defn stop []
  (qx.core/stop))
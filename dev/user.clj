(ns user
  (:require qx.core
            sample))

(defn start []
  (qx.core/start sample/config))

(defn stop []
  (qx.core/stop))
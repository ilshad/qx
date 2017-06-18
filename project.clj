(defproject ilshad/qx "0.1.0-SNAPSHOT"
  :description ""
  :url "http://github.com/ilshad/qx"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha16"]
                 [io.pedestal/pedestal.service "0.5.2"]]
  :profiles {:dev {:dependencies [[com.datomic/datomic-free "0.9.5561.50"]]}})

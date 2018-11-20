(defproject org.purefn/tuttle "0.1.0-SNAPSHOT"
  :description "Fetch Kubernetes configmaps/secrets as a service without auth."
  :url "https://github.com/theladders/tuttle"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [bidi "2.1.4" :exclusions [ring/ring-core]]
                 [cheshire "5.8.1"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [ring "1.7.1"]]

  :main org.purefn.tuttle.system
  
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [com.stuartsierra/component.repl "0.2.0"]]
                   :source-paths ["dev"]}})

(defproject org.purefn/tuttle "0.1.0-SNAPSHOT"
  :description "Fetch Kubernetes configmaps/secrets as a service without auth."
  :url "https://github.com/theladders/tuttle"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 ;; older version of cheshire which breaks things
                 [bidi "2.0.17"]
                 [com.stuartsierra/component "0.3.2"]
                 [com.taoensso/timbre "4.10.0"]
                 [ring/ring-codec "1.1.0"]
                 [ring/ring-core "1.6.3"]
                 [ring/ring-devel "1.6.3"]
                 [ring/ring-json "0.4.0"]]

  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.11"]
                                  [com.stuartsierra/component.repl "0.2.0"]]
                   :source-paths ["dev"]}})

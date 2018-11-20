(ns org.purefn.tuttle.system
  (:require [com.stuartsierra.component :as component]
            [org.purefn.tuttle :as tuttle]
            [taoensso.timbre :as log])
  (:gen-class))

(defonce running (atom nil))

(defn -main
  []
  (let [sys (component/system-map
             :server (tuttle/server))
        run (promise)]
    (reset! running (component/start sys))
    (log/info "Harry Tuttle, heating engineer at your service.")
    (deref run)))


(ns org.purefn.tuttle
  (:require [bidi.ring :as bidi-ring]
            [clojure.string :as str]
            [com.stuartsierra.component :as component]
            [org.purefn.tuttle.kube :as kube]
            [org.purefn.tuttle.sh :as sh]
            [ring.adapter.jetty :as jetty]
            [ring.middleware.keyword-params :refer (wrap-keyword-params)]
            [ring.middleware.params :refer (wrap-params)]
            [ring.middleware.stacktrace :refer (wrap-stacktrace)]
            [ring.util.response :refer (response)]
            [taoensso.timbre :as log]))

;;--------------------------------------------------------------------------------
;; Handlers

(defn- health-check
  [req]
  (response "I'm alive!"))

(defn- not-found
  [{:keys [request-method uri]}]
  (let [msg (format "%s %s not found" request-method uri)]
    (log/warn msg)
    {:status 404
     :body msg}))

(defn- list-objects
  [kf _]
  (->> (kf)
       (map (comp first #(str/split % #"/") :file))
       (distinct)
       (str/join "\n")))

(defn- list-keys
  [kf {:keys [params]}]
  (->> (kf (:name params))
       (str/join "\n")))

(defn- value
  [kf {:keys [params]}]
  (->> (kf (:name params) (:key params))))

(defn fetch
  [kf f]
  (comp response
        (fn [req]
          (f kf req))))

(defn shell-script
  [req]
  (log/info (:params req))
  (-> (sh/shell-script (get-in req [:params "prefix"] "."))
      (response)))

;;--------------------------------------------------------------------------------
;; Routes

(def routes
  [""
   [["/"
     {"health" {:get health-check}

      "configmap"
      {:get {"/" (fetch kube/configmaps list-objects)
             ["/" :name] (fetch kube/configmap-keys list-keys)
             ["/" :name "/" :key] (fetch kube/configmap-value value)}}

      "secret"
      {:get {"/" (fetch kube/secrets list-objects)
             ["/" :name] (fetch kube/secret-keys list-keys)
             ["/" :name "/" :key] (fetch kube/secret-value value)}}

      "sh" {:get shell-script}}]

    [true not-found]]])

(def app
  (-> (bidi-ring/make-handler routes)
      (wrap-params)
      (wrap-stacktrace)))

;;--------------------------------------------------------------------------------
;; Component

(defrecord Server
    []

  component/Lifecycle
  (start [this]
    (assoc this :server
           (jetty/run-jetty #'app
                            {:port 8080
                             :join? false})))

  (stop [this]
    (when-let [server (:server this)]
      (.stop server))
    (assoc this :server nil)))

(defn server
  []
  (Server.))

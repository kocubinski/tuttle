(ns org.purefn.tuttle.kube
  (:require [cheshire.core :as json]
            [clojure.java.shell :refer [sh]]
            [clojure.string :as str])
  (:import [java.util Base64]))

;;--------------------------------------------------------------------------------
;; Secrets

(defn decode
  [s]
  (-> (Base64/getDecoder)
      (.decode s)
      (String.)
      (str/trim)))

(defn parse-secret
  [secret]
  (map (fn [name [k v]]
         {:file (str name "/" k)
          :data (decode v)})
       (repeat (get-in secret ["metadata" "name"]))
       (get secret "data")))

(defn secrets
  []
  (-> (sh "kubectl" "get" "secrets" "-o" "json")
      (:out)
      (json/parse-string)
      (get "items")
      (->> (mapcat parse-secret)
           (filter (comp not
                         #(str/starts-with? % "default-token")
                         :file)))))

(defn secret-keys
  [n]
  (-> (sh "kubectl" "get" "secret" n "-o" "json")
      (:out)
      (json/parse-string)
      (get "data")
      (keys)))

(defn secret-value
  [n k]
  (some-> (sh "kubectl" "get" "secret" n "-o" "json")
          (:out)
          (json/parse-string)
          (get-in ["data" k])
          (decode)))

;;----------------------------------------------------------------------
;; ConfigMaps

(defn parse-config
  [config]
  (map (fn [name [k v]]
         {:file (str name "/" k)
          :data (str/trim v)})
       (repeat (get-in config ["metadata" "name"]))
       (get config "data")))

(defn configmaps
  []
  (-> (sh "kubectl" "get" "configmap" "-o" "json")
      (:out)
      (json/parse-string)
      (get "items")
      (->> (mapcat parse-config))))

(defn configmap-keys
  [n]
  (-> (sh "kubectl" "get" "configmap" n "-o" "json")
      (:out)
      (json/parse-string)
      (get "data")
      (keys)))

(defn configmap-value
  [n k]
  (some-> (sh "kubectl" "get" "configmap" n "-o" "json")
          (:out)
          (json/parse-string)
          (get-in ["data" k])
          (str/trim)))

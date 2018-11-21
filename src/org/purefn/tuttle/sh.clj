(ns org.purefn.tuttle.sh
  (:require [clojure.string :as str]
            [org.purefn.tuttle.kube :as kube]))

(defn header
  [prefix]
  (let [configs (str prefix "/configs")
        secrets (str prefix "/secrets")]
    (str
     "#!/bin/sh
mkdir -p " configs "
mkdir -p " secrets "
chown `whoami`:`id -g` " configs "
chown `whoami`:`id -g` " secrets)))

(defn shell-script
  [prefix]
  (let [uuid (str (java.util.UUID/randomUUID))]
    (->> 
     (concat
      (map (fn [{:keys [file data]}]
             (str "
mkdir -p " prefix "/configs/" (first (str/split file #"/")) "
cat <<" uuid " > " (str prefix "/configs/" file) "
" data "
" uuid))
           (kube/configmaps))
      (map (fn [{:keys [file data]}]
             (str "
mkdir -p " prefix "/secrets/" (first (str/split file #"/")) "
cat <<" uuid " > " (str prefix "/secrets/" file) "
" data "
" uuid))
           (kube/secrets)))
     (apply str (header prefix)))))

(ns org.purefn.tuttle.sh
  (:require [clojure.string :as str]
            [org.purefn.tuttle.kube :as kube]))

(defn header
  [prefix]
  (let [configs (str prefix "/configs")
        secrets (str prefix "/secrets")]
    (str
     "#!/bin/bash
mkdir -p " configs "
mkdir -p " secrets "
chown `whoami`:`id -g -n` " configs "
chown `whoami`:`id -g -n` " secrets)))

(defn shell-script
  [prefix]
  (->> 
   (concat
    (map (fn [{:keys [file data]}]
           (str "
mkdir -p " prefix "/configs/" (first (str/split file #"/")) "
echo '" data "' > " (str prefix "/configs/" file)))
         (kube/configmaps))
    (map (fn [{:keys [file data]}]
           (str "
mkdir -p " prefix "/secrets/" (first (str/split file #"/")) "
echo '" data "' > " (str prefix "/secrets/" file)))
         (kube/secrets)))
   (apply str (header prefix))))

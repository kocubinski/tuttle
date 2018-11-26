(ns org.purefn.tuttle.sh
  (:require [clojure.string :as str]
            [org.purefn.tuttle.kube :as kube]))

(defn header
  [prefix]
  (let [configs (str prefix "/configs")
        secrets (str prefix "/secrets")]
    (str
     "#!/bin/sh
set -e +x

SUDO=$(command -v sudo >/dev/null 2>&1 && echo sudo || true)
CONFIGS=" configs "
SECRETS=" secrets "
[ -d $CONFIGS ] || $SUDO mkdir -p $CONFIGS && $SUDO chown `whoami`:`id -g` $CONFIGS
[ -d $SECRETS ] || $SUDO mkdir -p $SECRETS && $SUDO chown `whoami`:`id -g` $SECRETS")))

(defn shell-script
  [prefix]
  (let [uuid (str (java.util.UUID/randomUUID))]
    (->> 
     (concat
      (map (fn [{:keys [file data]}]
             (str "
mkdir -p $CONFIGS/" (first (str/split file #"/")) "
cat << '" uuid "' > $CONFIGS/" file "
" data "
" uuid))
           (kube/configmaps))
      (map (fn [{:keys [file data]}]
             (str "
mkdir -p $SECRETS/" (first (str/split file #"/")) "
cat << '" uuid "' > $SECRETS/" file "
" data "
" uuid))
           (kube/secrets)))
     (apply str (header prefix)))))


#!/bin/bash
set -e

[[ -n $REPO ]] || REPO=kocubinski
: ${NAMESPACE?"Need to set NAMESPACE"}

IMAGE=$REPO/tuttle:latest

docker build -t $IMAGE .
docker push $IMAGE

cat deployment.yaml \
  | sed -e "s|@IMAGE@|$IMAGE|g;" \
  | kubectl apply -n $NAMESPACE -f -

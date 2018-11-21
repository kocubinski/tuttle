#!/bin/bash
set -e

[[ -n $REPO ]] || REPO=kocubinski

IMAGE=$REPO/tuttle:latest

docker build -t $IMAGE .
docker push $IMAGE

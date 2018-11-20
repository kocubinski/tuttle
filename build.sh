#!/bin/bash

: ${REPO?"Need to set REPO"}

lein clean
lein uberjar

docker build -t $REPO/tuttle:latest .
docker push $REPO/tuttle:latest

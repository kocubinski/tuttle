#!/bin/bash

# required for this pod to have proper service account credentials

kubectl create rolebinding default-view \
	--clusterrole=view \
	--serviceaccount=platform:default \
	--namespace=platform

kubectl apply -f secret-reader.yaml

kubectl create rolebinding default-secrets \
	--clusterrole=secret-reader \
	--serviceaccount=platform:default \
	--namespace=platform

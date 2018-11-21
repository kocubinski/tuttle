#!/bin/bash
# required for this deployment's pod to have proper service account
# credentials

: ${NAMESPACE?"Need to set NAMESPACE"}

kubectl create rolebinding default-view \
	--clusterrole=view \
	--serviceaccount=$NAMESPACE:default \
	--namespace=$NAMESPACE

kubectl apply -f secret-reader.yaml

kubectl create rolebinding default-secrets \
	--clusterrole=secret-reader \
	--serviceaccount=$NAMESPACE:default \
	--namespace=$NAMESPACE

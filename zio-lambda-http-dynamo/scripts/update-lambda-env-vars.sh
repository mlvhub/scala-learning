#!/usr/bin/env bash

functionName=$1

aws lambda update-function-configuration --function-name $functionName \
	--environment "Variables={$(cat .env | sed '/^$/d'  | sed '$!s/$/,/' | tr -d "\n")}"

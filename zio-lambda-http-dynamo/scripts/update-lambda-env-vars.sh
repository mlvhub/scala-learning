#!/usr/bin/env bash

functionName=$1

aws lambda update-function-configuration \
    --function-name $functionName \
    --timeout 10 \
    --memory-size 256 \
	--environment "Variables={$(cat .env | sed '/^$/d'  | sed '$!s/$/,/' | tr -d "\n")}"

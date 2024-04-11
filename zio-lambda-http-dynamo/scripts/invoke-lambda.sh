#!/usr/bin/env bash

set -euo pipefail

functionName=$1

aws lambda invoke --function-name $functionName --payload "$(cat ./scripts/test.json)" response.txt --cli-binary-format raw-in-base64-out
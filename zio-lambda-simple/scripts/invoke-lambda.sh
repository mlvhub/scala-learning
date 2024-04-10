#!/usr/bin/env bash

set -euo pipefail

aws lambda invoke --function-name ziolambdasimple --payload "$(cat ./scripts/test.json)" response.txt --cli-binary-format raw-in-base64-out
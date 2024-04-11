#!/usr/bin/env bash

env $(cat .env | xargs) scala-cli run . --main-class ziolambda.MainApp
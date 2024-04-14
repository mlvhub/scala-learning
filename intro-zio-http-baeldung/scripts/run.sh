#!/usr/bin/env bash

env $(cat .env | xargs) scala-cli . --main-class simple_api.MainApp
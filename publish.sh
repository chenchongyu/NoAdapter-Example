#!/usr/bin/env bash

# Be sure your script exit whenever encounter errors
set -e

source ./cmc.sh

gradle clean build upload

./postBuild.sh

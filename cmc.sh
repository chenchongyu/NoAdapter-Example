#!/usr/bin/env bash

# Be sure your script exits whenever encounter errors
set -e

# Be sure your charset is correct. eg: zh_CN.UTF-8
export LC_ALL=en_US.UTF-8
export LANG=en_US.UTF-8
export LANGUAGE=en_US.UTF-8

# Use CMC Environment Variables to set yours
# See http://wiki.baidu.com/pages/viewpage.action?pageId=480000071 to find more.
export PATH=$GRADLE_BIN_V511:$JAVA_BIN_V11:$PATH

export JAVA_HOME=$JAVA_HOME_V11

export GRADLE_HOME=$GRADLE_HOME_V511

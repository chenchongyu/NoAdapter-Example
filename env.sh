#!/bin/bash

# Environment for build cloud
export JAVA_HOME=/home/scmtools/buildkit/jdk-1.8u92/
export ANDROID_SDK_ROOT=/home/scmtools/buildkit/android-sdk/
export ANDROID_SDK_HOME=~/
export GRADLE=/home/scmtools/buildkit/gradle/gradle_4.1/bin/gradle
export PATH=/home/scmtools/buildkit/jdk-1.8u92/bin:$PATH
export ANDROID_NDK_HOME=/home/scmtools/buildkit/android-sdk/ndk-bundle
unset ANDROID_HOME
echo 'sdk.dir=/home/scmtools/buildkit/android-sdk/' > local.properties

export BUILD_REPO_WS=$PWD                      # 获取代码根目录
export GRADLE_USER_HOME=$BUILD_REPO_WS/.gradle # 指定gradle依赖下载路径为代码库根目录下的.gradle目录

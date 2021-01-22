#!/bin/bash
rm -rf output
mkdir -p output
#cp build../gradlew output
cd output
#tar cvzf android.tar.gz *
cd ..
java -version
export PATH=$JDK_8_162_BIN:$PATH
echo "after set java version"
java -version
echo "set again"
export JAVA_HOME=/home/scmtools/buildkit/jdk-1.8u92
export PATH=JAVA_HOME:$PATH
java -version

library=$1
./gradlew -v
if [ library == "" ]; then
  ./gradlew upload
else
  ./gradlew :library:upload

fi

if [ $? -ne 0 ]; then
  exit 1
fi
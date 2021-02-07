#!/usr/bin/env bash
#云端打包脚本

echo  "**************************打包开始**************************"
#!/bin/bash
source ./env.sh
echo  "**************************1**************************"
#source ./version.sh
mkdir -p output
echo  "**************************2**************************"
chmod +x ./gradlew
echo  "**************************3**************************"
./gradlew assembleRelease -PPIPELINE=true
echo  "**************************4**************************"

if [ $? -ne 0 ]; then
  exit 1
fi

#cp ./app/build/outputs/apk/release/*.apk ./output
#cp ./app/build/outputs/mapping/release/mapping.txt ./output

echo -e "**************************打包完成**************************"

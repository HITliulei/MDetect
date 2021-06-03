#!/bin/bash

echo 'build mdetect'

# 构建项目
mvn clean package
cd MProgrammingFramework ; mvn install


# 构建基础镜像

cd ../utils/makePython3Image ; ./run.sh
cd ../../
# 构建初始化镜像

cd initData ; docker build -t framework_ts-a-datainit .


echo 'build end'
#!/bin/bash


BRANCH=framework
EX_TIMES=10
EX_NAME=1
EX_IP=172.31.43.94

if [[ $# == 2 ]]; then
    echo 'ex name : ' $1
    echo 'ex times: ' $2
    EX_NAME=$1
    EX_TIMES=$2
fi


sleep 2s
echo 'begin ex'
# cp /home/liulei/SmellEx/smellEx/a.txt /home/liulei/data/command/$EX_NAME
cp /home/ubuntu/smellex/smellEx/a.txt /home/ubuntu/data/command/$EX_NAME
for i in $( seq 1 $EX_TIMES )
do
  echo 'the ' $i ' times ex start'
  # 1. delete all service in cluster and some tools in namespace -n default
  echo -e "\033[31m 1. delete all service in cluster \033[0m"
  kubectl delete service -n default --all
  kubectl delete deployment -n default --all
  kubectl delete pods --force --grace-period=0 -n default --all
  # 2. delete Deployment tools and init tools
  echo -e "\033[31m 2. delete tools \033[0m"
  docker rm -f ts-Deployment-service

  # 3. inti zipkin and mongo and mysql， zipkin未来可能不需要了
  echo -e "\033[31m 3. inti zipkin, mongo, mysql, and deploy-tools \033[0m"
  kubectl apply -f ts-Deployment-before.yaml
  docker run -itd --name ts-Deployment-service -p 32001:32001 --restart=always framework_ts-a-Deployment
  sleep 30s

  # 4. Deployment all the service, tools,
  echo  -e "\033[31m 4. deploy all service\033[0m"

  # 4.1. 部署所有服务
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32001/Deployment
#  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32001/deployexit
  # 4.2. 部署初始化数据服务 3. 部署需求给予服务
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32001/deploycommand
  sleep 3m

  # 5. init data
  echo -e "\033[31m 5. deploy complete, and now init data \033[0m"
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataUser
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataPrice
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataTravel

  sleep 5s
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/clearTrace
  sleep 15s

  # 6. 启动性能监测工具
  echo -e "\033[31m 6. Open the monitor performance tool \033[0m"
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getcollectNodeInfo/$EX_NAME/$i
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getAllPodsInfo/$EX_NAME/$i
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getAllNetInfo/$EX_NAME/$i

  sleep 20s

  # 7. give command and collect data
  echo -e "\033[31m 7. give the command and collect the data \033[0m"
  python3 sendrequest.py $EX_NAME $i
  echo 'the ' $i ' times ex finished'

  sleep 20s
  # 8. 停止收集
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/stopCollectData/$EX_NAME/$i
  sleep 30s
done
echo -e "\033[31m 实验结束 \033[0m"
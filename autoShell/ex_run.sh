#!/bin/bash


EX_TIMES=10
EX_NAME=1
EX_BRANCH=framework
EX_IP=127.0.0.1
CENTER_PORT=18080

if [[ $# == 3 ]]; then
    echo 'ex name : ' $1
    echo 'ex times: ' $2
    echo 'branch: ' $3
    EX_NAME=$1
    EX_TIMES=$2
    EX_BRANCH=$3
fi



sleep 2s
echo 'begin ex'
# cp /home/liulei/SmellEx/smellEx/a.txt /home/liulei/data/command/$EX_NAME
echo -e "\033[31m register service Info \033[0m"
curl -X POST -H "Content-Type:application/json" -D '{"gitUrl":"http://192.168.1.104:12345/BoyLei/train-ticket.git","branch":$EX_BRANCH}' http://$EX_IP:$CENTER_PORT/api/v1/serviceController/register
for i in $( seq 1 $EX_TIMES )
do
  echo 'the ' $i ' times ex start'
  echo -e "\033[31m 1. delete all service in cluster \033[0m"
  kubectl delete service -n default --all
  kubectl delete deployment -n default --all
  kubectl delete pods --force --grace-period=0 -n default --all

  echo -e "\033[31m 2. remove all tools \033[0m"
  docker rm -f ts-Deployment-service


  echo -e "\033[31m 3. inti mongo, mysql, and deploy-tools \033[0m"
  kubectl apply -f ts-Deployment-before.yaml
  docker run -itd --name ts-Deployment-service -p 32001:32001 --restart=always framework_ts-a-Deployment
  sleep 30s

  echo  -e "\033[31m 4. deploy all service\033[0m"
  echo  -e "\033[31m 4.1 deploy all microservice\033[0m"
  curl -X GET http://$EX_IP:$CENTER_PORT/api/v1/deployment/deployment?name=$EX_NAME&times=$i&branch=%EX_BRANCH
#  echo  -e "\033[31m 4.1 deploy exit microservice\033[0m"
#  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32001/deployexit
  echo  -e "\033[31m 4.2 deploy command assembly \033[0m"
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:$CENTER_PORT/api/v1/deployment/deploymentCommand
  sleep 3m

  # 5. init data
  echo -e "\033[31m 5. deploy complete, and now init data contains user price Travel\033[0m"
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataUser
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataPrice
  curl -X GET -H "name:$EX_NAME" -H "times:$i" http://$EX_IP:32002/initDataTravel


  echo -e "\033[31m 6. Open the monitor performance tool \033[0m"
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getcollectNodeInfo/$EX_NAME/$i
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getAllPodsInfo/$EX_NAME/$i
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/getAllNetInfo/$EX_NAME/$i
  sleep 20s


  echo -e "\033[31m 7. begin ex: give the command and collect the data \033[0m"
  python3 sendrequest.py $EX_NAME $i
  echo 'the ' $i ' times ex finished'

  sleep 20s
  # 8. 停止收集
  curl -X GET http://$EX_IP:32223/api/v1/collectdata/stopCollectData/$EX_NAME/$i
  sleep 30s
done
echo -e "\033[31m 实验结束 \033[0m"
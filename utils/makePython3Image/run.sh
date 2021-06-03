#!/bin/zsh

docker rmi lei_python:3
docker build -t lei_python:3 .

docker tag lei_python:3 192.168.1.104:5000/lei_python:3

docker push 192.168.1.104:5000/lei_python:3


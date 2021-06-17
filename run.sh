#!/bin/bash
IMAGE_NAME=authorizer:latest
CONTAINER_NAME=authorizer
sudo docker build -t $IMAGE_NAME . && \
echo -ne "\nRemoving container if exists => " && \
sudo docker rm -f $CONTAINER_NAME
echo -e "\n:: Running Authorizer ::\n" && \
sudo docker run -it --name $CONTAINER_NAME $IMAGE_NAME
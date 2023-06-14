#!/bin/bash

# JAR 파일 경로
jar_path="/home/ubuntu/deploy/deploy/back-0.0.1-SNAPSHOT.jar"

# 실행 중인 프로세스 ID 확인
pid=$(pgrep -f "$jar_path")

if [ -n "$pid" ]; then
  echo "Stopping the Spring server..."
  kill "$pid"
  sleep 5

  # 종료 여부 확인
  pid=$(pgrep -f "$jar_path")
  if [ -n "$pid" ]; then
    echo "Failed to stop the Spring server. Killing the process forcefully..."
    pkill -f "$jar_path"
    sleep 5
  else
    echo "The Spring server has been stopped."
  fi
else
  echo "The Spring server is not running."
fi

echo "Starting the Spring server..."
nohup java -jar -DAWS_ACCESS_KEY=${AWS_ACCESS_KEY} -DAWS_SECRET_KEY=${AWS_SECRET_KEY} -DAWS_S3_BUCKET=${AWS_S3_BUCKET}  -DSOLAPIKEY=${SOLAPIKEY} -DSOLAPIKEY=${SOLAPISECRET} -DPARTNER_OFPARTNER=${OFPARTNER} -DCLIENTID=${CLIENTID} -DREDIRECTURL=${REDIRECTURL} -DYOLDA=${MAINCH} -DDOMAIN=${DOMAIN} "$jar_path" &


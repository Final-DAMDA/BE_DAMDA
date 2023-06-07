#!/bin/bash

# JAR 파일 경로
jar_path="/home/ubuntu/deploy/deploy/back-0.0.1-SNAPSHOT.jar"

# 실행 중인 프로세스 ID 확인
pid=$(pgrep -f "$jar_path")

if [ -n "$pid" ]; then
  echo "Stopping the Spring server..."
  kill "$pid"
  sleep 5
fi

echo "Starting the Spring server..."
nohup java -jar -DAWS_ACCESS_KEY=${AWS_ACCESS_KEY} -DAWS_SECRET_KEY=${AWS_SECRET_KEY} -DAWS_S3_BUCKET=${AWS_S3_BUCKET} -DCLIENTID=${CLIENTID} -DREDIRECTURL=${REDIRECTURL}  "$jar_path" > log.out 2>&1


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
nohup java -jar "$jar_path" &

# 백그라운드 프로세스의 종료 대기
wait

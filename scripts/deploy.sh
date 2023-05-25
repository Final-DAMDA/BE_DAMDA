
# JAR 파일 경로
jar_path="/home/ubuntu/deploy/deploy/back-0.0.1-SNAPSHOT.jar"

# 실행 중인 프로세스 종료
pkill -f "$jar_path"

# 스프링 서버 다시 실행
nohup java -jar "$jar_path" &
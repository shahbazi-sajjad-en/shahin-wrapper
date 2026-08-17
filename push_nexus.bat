set app=shahin-wrapper:v1
@REM ./mvn clean install
docker build -t %app% .
docker tag %app% docker.ham-sun.com/docker-hosted/%app%
docker push docker.ham-sun.com/docker-hosted/%app%
@REM docker pull docker.ham-sun.com/docker-hosted/shahin-wrapper:v1
@REM docker run  -d  --network="host"  docker_image


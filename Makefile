ASSEMBLY := target/scala-2.12/geotrellis-pointcloud-server-assembly-0.1.2-SNAPSHOT.jar

${ASSEMBLY}: $(call rwildcard, src/, *.scala, build.sbt)
	./sbt assembly
	@touch -m ${ASSEMBLY}

run:
	./sbt "run --public-url http://localhost:9000/"

assembly:
	./sbt assembly

run-assembly: ${ASSEMBLY}
	java -jar ${PWD}/target/scala-2.12/geotrellis-pointcloud-server-assembly-0.1.2-SNAPSHOT.jar --public-url "http://localhost:9000/"

docker:
	./sbt docker

run-docker:
	docker-compose up


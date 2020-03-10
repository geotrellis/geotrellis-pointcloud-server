ASSEMBLY := target/scala-2.12/geotrellis-pointcloud-server-assembly-0.1.0-SNAPSHOT.jar

${ASSEMBLY}: $(call rwildcard, src/, *.scala, build.sbt)
	./sbt assembly
	@touch -m ${ASSEMBLY}

run:
	./sbt "run --public-url http://localhost:9000/"

assembly:
	./sbt assembly

run-assembly: ${ASSEMBLY}
	java -jar ${PWD}/target/scala-2.12/geotrellis-pointcloud-server-assembly-0.1.0-SNAPSHOT.jar --public-url "http://localhost:9000/"

docker:
	./sbt docker

run-docker:
	docker run --rm -v ${PWD}/data:/app/data -p 9000:9000 geotrellis/geotrellis-pointcloud-server:latest --public-url http://localhost:9000/


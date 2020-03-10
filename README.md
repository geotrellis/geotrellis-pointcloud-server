# GeoTrellis PointCloud server

To run this demo you can use the following Makefile commands:

```bash
# to run from the sbt shell
$ make run

# to run assembly jar locally
$ make run-jar

# build docker
$ make docker

# run server in a docker container
$ make run-docker
```

## Makefile

Makefile contains some shortcuts to simplify the demo run.

| Command          | Description
|------------------|------------------------------------------------------------|
|run               |Run server from sbt                                         |
|assembly          |Build assembly jar                                          |
|run-assembly      |Run assembly jar                                            |
|docker            |Build a docker container                                    |
|run-docker        |Run assembly jar inside a docker container                  |

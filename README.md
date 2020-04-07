# GeoTrellis PointCloud OGC Server 

[![CircleCI](https://circleci.com/gh/geotrellis/geotrellis-pointcloud-server.svg?style=svg)](https://circleci.com/gh/geotrellis/geotrellis-pointcloud-server)
[![Join the chat at https://gitter.im/geotrellis/geotrellis](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/geotrellis/geotrellis?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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

| Command          | Description                                                              |
|------------------|--------------------------------------------------------------------------|
|run               |Run server from sbt                                                       |
|assembly          |Build assembly jar                                                        |
|run-assembly      |Run assembly jar                                                          |
|docker            |Build a docker container                                                  |
|run-docker        |Run assembly jar inside a docker container, alias for `docker-compose up` |
|run-docker-raw    |`docker-compose up` alternative but without docker-compose usage          |

## AWS S3 Configuration

For the proper work with [usgs-lpc-ny-cl-ess-lchamp layer](./src/main/resources/application.conf#L222) it is necessary to 
configure AWS S3 Credentials via env variables or via `~/.aws/credentials` file. The region of this layer is `us-west-2`.

PDAL also requires `AWS_CONFIG_FILE` variable to point to the `~/.aws/credentials` file. 
Check out the [docker-compose.yaml](./docker-compose.yaml) file for the min necessary configuration.

If you want to view the [usgs-lpc-ny-cl-ess-lchamp layer](./src/main/resources/application.conf#L222) S3 layer, please modify the
application.conf file to include this layer in the services descriptions: 

```yaml
layer-definitions = [
  ${layers.red-rocks},
  ${layers.usgs-lpc-ny-cl-ess-lchamp}
]
```

## Ingest 

Some ingest docs can be found [here](https://github.com/connormanning/entwine#usage). This section would briefly describe the ingest
process as well with some important parameters descriptions.

The simple ingest can be launched via the following command:

```bash
mkdir ~/entwine
docker run -it -v ~/entwine:/entwine connormanning/entwine build \
    -i https://data.entwine.io/red-rocks.laz \
    -o /entwine/red-rocks
```

* All extra parameters supported by the catalog build process are described [here](https://github.com/connormanning/entwine/blob/master/app/build.cpp#L54-L196).
* In order to update the same catalog, preset the [bounds](https://github.com/connormanning/entwine/blob/master/app/build.cpp#L97) parameter. 
It will make possible to ingest some new files into the same catalog that are inside of the predefined bounds.
* [span](https://github.com/connormanning/entwine/blob/master/app/build.cpp#L79) allows to specify the amount of voxels per dimension, a pretty 
important for the catalog read performance parameter. 


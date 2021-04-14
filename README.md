# botdiril ~~400~~ 350
Botdiril Partially Rewritten (Again)

Documentation WIP.

## Building the docker image

GitHub packages is a temporary solution until PlutoEngine is uploaded to Maven central.

```shell
DOCKER_BUILDKIT=1 docker build --build-arg githubPackagesKey="<githubPackagesKey>" -t botdiril350 .
```

## Running in docker

```shell
docker secret create botdiril350.config.json <pathToSettings>.json
```

```shell
docker service create --secret src=botdiril350.config.json,target='/app/botdiril350-350.0/settings.json' botdiril350
```
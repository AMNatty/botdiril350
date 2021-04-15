# botdiril ~~400~~ 350
Botdiril Partially Rewritten (Again)

Documentation WIP.

## Dependencies

All dependencies except [PlutoEngine](https://github.com/493msi/plutoengine/packages/730518) should
be automatically pulled by Gradle.

## Running manually

```shell
./gradlew run
```

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
docker service create --secret src=botdiril350.config.json,target='/app/botdiril350/settings.json' botdiril350
```
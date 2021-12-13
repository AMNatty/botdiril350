FROM gradle:jdk16 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle --no-daemon -PpackagesKey=\"$githubPackagesKey\" --stacktrace build
WORKDIR /home/gradle/src/build/distributions/
RUN tar -xvf botdiril350.tar

FROM openjdk:16
COPY --from=builder /home/gradle/src/build/distributions/botdiril350 /app/botdiril350
WORKDIR /app/botdiril350/
CMD bin/botdiril350
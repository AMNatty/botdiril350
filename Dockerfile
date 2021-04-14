FROM gradle:jdk15 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ARG githubPackagesKey
ENV GITHUB_PACKAGES_KEY $githubPackagesKey
RUN gradle --stacktrace build

FROM openjdk:15
COPY --from=builder /home/gradle/src/build/distributions/botdiril350-350.0.tar /app/
WORKDIR /app/
RUN tar -xvf botdiril350-350.0.tar
WORKDIR /app/botdiril350-350.0/
CMD bin/botdiril350
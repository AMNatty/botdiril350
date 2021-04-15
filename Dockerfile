FROM gradle:jdk15 as builder
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ARG githubPackagesKey
ENV GITHUB_PACKAGES_KEY $githubPackagesKey
RUN gradle --stacktrace build
WORKDIR /home/gradle/src/build/distributions/
RUN tar -xvf botdiril350-350.4.tar

FROM openjdk:15-alpine
COPY --from=builder /home/gradle/src/build/distributions/botdiril350-350.4 /app/botdiril350-350.4
WORKDIR /app/botdiril350-350.4/
CMD bin/botdiril350
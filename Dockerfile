FROM gradle:8-jdk16 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean :launcher:installDist --no-daemon

FROM openjdk:16
RUN mkdir /app
COPY --from=build /home/gradle/src/launcher/build/install/launcher/ /app/
ENTRYPOINT ["java","-jar","/app/bin/launcher"]

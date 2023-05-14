# stage 1
# start with a base image containing java runtime
FROM openjdk:11-slim as build

# add maintainer info
LABEL authors="vula"

# the application's jar file
ARG JAR_FILE

# add the application's jar to the container
COPY ${JAR_FILE} app.jar

# unpackage jar file
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf /app.jar)

# stage 2
# same java runtime
FROM openjdk:11-slim

# add volume pointing to /tmp
VOLUME /tmp

# copy unpackaged application to new container
ARG DEPENDENCY=/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# execute the application
ENTRYPOINT ["java", "-cp", "app:app/lib/*", "com.vula.license.LicenseServiceApplication"]

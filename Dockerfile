FROM openjdk:21-jdk-slim
VOLUME /tmp
ARG JAVA_OPTS
ENV JAVA_OPTS=$JAVA_OPTS
COPY build/libs/duke-0.0.1-SNAPSHOT.jar duke.jar
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar duke.jar
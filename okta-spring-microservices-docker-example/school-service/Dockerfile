FROM alpine:latest
RUN apk add openjdk11
VOLUME /tmp
ADD target/*.jar app.jar
ENV JAVA_OPTS=-Djava.security.egd=file:/dev/./urandom
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]

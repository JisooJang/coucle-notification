FROM openjdk:8-jre-alpine
COPY build/libs/coucle-notification-0.0.1-SNAPSHOT.jar app.jar
ARG SPRING_ENV="local"
ENV SPRING_PROFILES_ACTIVE ${SPRING_ENV}
ENTRYPOINT java -Dspring.profiles.active="$SPRING_PROFILES_ACTIVE" /app.jar
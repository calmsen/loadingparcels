FROM gradle:8.10.1-jdk21-alpine AS BUILD_IMAGE
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY . .
RUN gradle build -x test

FROM eclipse-temurin:21-jre-alpine
ENV APP_HOME=/usr/app

WORKDIR $APP_HOME
COPY --from=BUILD_IMAGE $APP_HOME/build/libs/*.jar $APP_HOME/app.jar

EXPOSE 8080
ENTRYPOINT exec java -jar app.jar
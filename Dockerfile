FROM maven:3-jdk-8-slim as builder

WORKDIR /tmp/eschool
COPY . ./

# ARG sonar_project_key
# ARG sonar_host_url
# ARG sonar_login

# RUN mvn clean verify sonar:sonar \
#   -Dsonar.projectKey=$sonar_project_key \
#   -Dsonar.host.url=$sonar_host_url \
#   -Dsonar.login=$sonar_login

# FROM gcr.io/distroless/java:8
RUN mvn package -DskipTests

FROM openjdk:8-jre-slim

WORKDIR /opt/eschool
COPY --from=builder /tmp/eschool/target/*.jar ./app.jar
EXPOSE 8080

ENTRYPOINT ["java"]
CMD ["-jar", "./app.jar"]

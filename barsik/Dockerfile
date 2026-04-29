FROM eclipse-temurin:8u462-b08-jdk-noble
MAINTAINER KETS
WORKDIR /opt
RUN apt-get update && apt-get install -y maven
COPY . /opt
RUN mvn clean install -U -Dmaven.test.skip -Dmaven.wagon.http.ssl.insecure=true

ENTRYPOINT ["mvn","spring-boot:run"]

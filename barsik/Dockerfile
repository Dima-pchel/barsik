FROM openjdk:11
MAINTAINER KETS
WORKDIR /opt
RUN apt-get update && apt-get install -y maven
COPY . /opt
RUN mvn clean install -U -Dmaven.test.skip -Dmaven.wagon.http.ssl.insecure=true

ENTRYPOINT ["mvn","spring-boot:run"]

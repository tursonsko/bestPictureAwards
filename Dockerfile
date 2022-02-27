FROM maven:3.6.3-jdk-11-slim AS build

COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM amazoncorretto:11
COPY  --from=build /home/app/target/bestPictureAwards-0.0.1-SNAPSHOT.jar /usr/local/lib/bestPictureAwards.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/bestPictureAwards.jar"]
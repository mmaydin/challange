FROM java:8
FROM maven:alpine

WORKDIR /app
ADD pom.xml /app
COPY data/application.properties /app
RUN mvn verify clean --fail-never

COPY . /app
RUN mvn -v
RUN mvn clean install -DskipTests
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/target/challenge-0.0.1.jar","--spring.config.location=file:/app/application.properties"]

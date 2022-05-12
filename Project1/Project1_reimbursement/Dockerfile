FROM maven:3.6.1-jdk-8-alpine AS MAVEN_BUILD_STAGE
COPY ./ ./
RUN mvn clean package -DskipTests

FROM openjdk:8-jre-alpine

COPY --from=MAVEN_BUILD_STAGE /target/Project1-1.0.jar /reimbursement.jar

ENTRYPOINT ["java","-jar","/reimbursement.jar"]
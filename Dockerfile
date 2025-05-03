FROM eclipse-temurin:21-jdk-alpine
WORKDIR /LearniverseConnect
COPY target/*.jar /LearniverseConnect/idata2306.jar
EXPOSE 8080
CMD ["java", "-jar", "idata2306.jar"]
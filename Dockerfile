FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/Mock_Lekcya-*.jar demo.jar
ENTRYPOINT ["java","-jar","demo.jar"]

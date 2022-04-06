FROM adoptopenjdk:11-jre-hotspot

RUN mkdir -p /app/
# cp spring-boot-web.jar /opt/app/app.jar
COPY build/libs/weather.jar /app/weather.jar

ENTRYPOINT ["java","-jar","/app/weather.jar"]

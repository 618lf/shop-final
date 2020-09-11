FROM amazoncorretto:8u265

MAINTAINER "lifeng"

WORKDIR /opt/lifeng/

ADD "${JAR_FILE}"

EXPOSE 8080

RUN chmod +x /opt/lifeng/start.sh

ENTRYPOINT ["java", "-jar -Xms800m -Xmx800m", "/opt/lifeng/shop-sample/shop-sample-1.0.0_final.jar"]

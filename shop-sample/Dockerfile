FROM amazoncorretto:8u265

MAINTAINER "lifeng"

WORKDIR /opt/lifeng/

ADD shop-sample-RELEASE.zip

EXPOSE 8080

ENTRYPOINT ["java", "-jar -Xms800m -Xmx800m", "/opt/lifeng/shop-sample/shop-sample-1.0.0_final.jar"]

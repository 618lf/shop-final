FROM amazoncorretto:8u265

MAINTAINER "lifeng"

RUN mkdir /opt/lifeng/

ADD shop-sample-RELEASE.tar /opt/lifeng/

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/opt/lifeng/shop-sample-RELEASE/shop-sample/shop-sample-1.0.0_final.jar"]

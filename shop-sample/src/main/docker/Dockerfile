FROM amazoncorretto:8u265

MAINTAINER "lifeng"

RUN mkdir -p /opt/lifeng/shop-sample

COPY config/ /opt/lifeng/shop-sample/config/
COPY lib/ /opt/lifeng/shop-sample/lib/
COPY shop-sample-1.0.0_final.jar  /opt/lifeng/shop-sample/
COPY statics/ /opt/lifeng/shop-sample/statics/

#EXPOSE 8080

#ENTRYPOINT ["java", "-jar", "/opt/lifeng/shop-sample/shop-sample-1.0.0_final.jar"]
FROM amazoncorretto:8u265

MAINTAINER "lifeng"

WORKDIR /opt/lifeng/

ADD "${JAR_FILE}"

EXPOSE 8080

RUN chmod +x /opt/lifeng/start.sh

CMD["/opt/lifeng/start.sh"]

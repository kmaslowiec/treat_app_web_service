FROM openjdk:13

COPY target/web_service-0.0.1-SNAPSHOT.jar web_app.jar
COPY startup.sh /startup.sh
RUN chmod +x /startup.sh
ENTRYPOINT ["/startup.sh"]
EXPOSE 8090d
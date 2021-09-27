FROM tomcat:jdk11-temurin
COPY target/userauth-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/app.war
RUN sh -c "touch /usr/local/tomcat/webapps/app.war"
EXPOSE 9901
ENTRYPOINT ["sh", "-c","java -Djava.security.edge=file:/dev/./urandom -jar /usr/local/tomcat/webapps/app.war"]
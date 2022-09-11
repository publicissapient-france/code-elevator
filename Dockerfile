FROM maven:3.8-jdk-11-slim
COPY . .
RUN mvn package --projects elevator-server --also-make

FROM jetty:9.4-jdk11-slim
COPY --from=0 elevator-server/target/*.war /var/lib/jetty/webapps/root.war

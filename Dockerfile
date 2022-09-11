FROM maven:3.8-jdk-11-slim as build
COPY . .
RUN mvn package --projects elevator-server --also-make

FROM jetty:11-jdk11-slim
COPY --from=build elevator-server/target/*.war /var/lib/jetty/webapps/root.war

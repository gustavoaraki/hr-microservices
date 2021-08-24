FROM maven:3-jdk-11 as builder

WORKDIR /app

COPY ./src /app/src
COPY pom.xml /app
RUN ls

RUN mvn -DskipTests=true clean package

RUN mkdir /app-jar-output
RUN cp /app/target/*.jar /app-jar-output

#################################################
## Estagio de criacao de imagem para producao  ##
#################################################
FROM openjdk:11-jre-slim

WORKDIR /app
COPY --from=builder /app-jar-output/ .

VOLUME /tmp

CMD java $JAVA_OPTS -jar $(ls *.jar)
FROM openjdk:alpine  AS certGenerator

ARG KEY_FILE
ARG CERT_FILE=""

RUN apk update && \
  apk add --no-cache openssl && \
  rm -rf /var/cache/apk/*
RUN openssl genrsa -des3 -out exampleprivate.key \
    -passout pass:allpassword \
    1024 \
 && openssl req \
    -new -key exampleprivate.key \
    -out example.csr \
    -passin pass:allpassword \
    -subj "/C=US/ST=Test/L=Test/O=Test/CN=localhost" \
 && openssl rsa \
    -in exampleprivate.key \
    -passin pass:allpassword \
    -out exampleprivateWithOutPass.key \
 && openssl x509 -req \
    -days 3650 \
    -in example.csr \
    -signkey exampleprivateWithOutPass.key \
    -out example.crt \
 && keytool -import -trustcacerts -noprompt \
    -file example.crt \
    -alias exampleCA \
    -keystore truststore.jks \
    -storepass allpassword \
 && openssl pkcs12 -export -in example.crt -inkey exampleprivateWithOutPass.key -passout pass:allpassword -certfile example.crt -name "examplecert" -out keystore.p12 \
 && keytool -importkeystore -noprompt  -srckeystore keystore.p12 -srcstoretype pkcs12 --srcstorepass allpassword -destkeystore keystore.jks -deststorepass allpassword -deststoretype JKS



FROM openjdk:11 AS builder
ENV APP_HOME=/root/dev/ssl
ENV MVN_BUILD_ARGS="spring-boot:run"

WORKDIR $APP_HOME

COPY . $APP_HOME/

#RUN ./mvnw clean package

# TODO remove COPY && Entrypoint (only used for Test / showcase, for real usacase copy the jks files to running layer)
COPY --from=certGenerator *.jks $APP_HOME/src/main/resources/
COPY --from=certGenerator *.jks $APP_HOME/src/test/resources/
# Use as debugging / Test Container (for prod use RUN and the running Layer Build Part)
ENTRYPOINT [ "sh", "-c", "./mvnw $MVN_BUILD_ARGS"]


## running Layer build
#FROM openjdk:11.0.8-jre-slim
#ARG BUILDER_APP_HOME=/root/dev/sslclient
#COPY --from=builder $BUILDER_APP_HOME/target/*-SNAPSHOT.jar app.jar
#COPY --from=certGenerator *.jks /cert/
# TODO add non-root user
#ENV JAVA_OPTS=""
#ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
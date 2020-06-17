FROM 233431242547.dkr.ecr.ap-south-1.amazonaws.com/sdx-core/openjdk:8

# Install groovy
# Use curl -L to follow redirects
ENV GROOVY_VERSION 3.0.4

RUN apk upgrade --update && \
    apk add --no-cache --virtual=build-dependencies curl ca-certificates wget unzip && \
    wget -q -O /tmp/groovy.zip "https://dl.bintray.com/groovy/maven/apache-groovy-binary-${GROOVY_VERSION}.zip" && \
    unzip -o -d "/tmp" "/tmp/groovy.zip" && \
    mv "/tmp/groovy-${GROOVY_VERSION}" "/usr/share/groovy" && \
    apk del build-dependencies && \
    rm -rf /tmp/* /var/cache/apk/*

#Add fixed startGroovy to work with busybox
#ADD startGroovy /usr/share/groovy/bin/

ENV GROOVY_HOME "/usr/share/groovy"
ENV PATH "$PATH:/usr/share/groovy/bin"

COPY target/SDX-CDP-*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

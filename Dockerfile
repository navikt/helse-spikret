FROM navikt/java:11
COPY build/libs/helse-spikret-1.0.0-all.jar app.jar
ENV JAVA_OPTS="-Dlogback.configurationFile=logback-remote.xml"

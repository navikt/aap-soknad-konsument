FROM gcr.io/distroless/java17
COPY target/*.jar "/app/app.jar"
ENV JAVA_OPTS --enable-preview 

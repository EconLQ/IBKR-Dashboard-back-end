# Use an official OpenJDK 11 image as the base image
ARG jdk=11
#FROM adoptopenjdk/${jdk}:latest
FROM openjdk:${jdk}

# Download dockerize to delay the start of the services
RUN wget -O /usr/local/bin/dockerize https://github.com/jwilder/dockerize/releases/download/v0.7.0/dockerize-linux-amd64-v0.7.0.tar.gz \
 && chmod +x /usr/local/bin/dockerize

# Set the working directory to /app
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

ENV WILDFLY_VERSION 16.0.0.Final

# Install Wildfly 16.0.0.Final
RUN curl -O https://download.jboss.org/wildfly/$WILDFLY_VERSION/wildfly-$WILDFLY_VERSION.tar.gz \
 && tar xzf wildfly-$WILDFLY_VERSION.tar.gz \
 && rm wildfly-$WILDFLY_VERSION.tar.gz \
 && mv wildfly-$WILDFLY_VERSION /opt/wildfly

# Set the environment variable JAVA_HOME to the Java 11 installation
ENV JAVA_HOME /usr/local/openjdk-11

# Set the environment variable PATH to include the Java 11 bin directory
ENV PATH $JAVA_HOME/bin:$PATH

# Set the environment variable WILDFLY_HOME to the Wildfly installation
ENV WILDFLY_HOME /opt/wildfly

# Set the environment variable PATH to include the Wildfly bin directory
ENV PATH $WILDFLY_HOME/bin:$PATH

# Create dir for mysql service in modules
RUN cd $WILDFLY_HOME/modules/system/layers/base/com/  \
    && mkdir -p "mysql/main"

# Copy mysql-connector jar and modules
COPY wildfly/modules/* $WILDFLY_HOME/modules/system/layers/base/com/mysql/main

# Add application's war to wildfly standalone deployments folder
#ADD target/IBKR-Dashboard-JEE8-1.0-SNAPSHOT.war $WILDFLY_HOME/standalone/deployments/

# Expose the Wildfly management port (9990)
EXPOSE 9990

# Run the Wildfly server
CMD /opt/wildfly/bin/standalone.sh -b 0.0.0.0 -bmanagement 0.0.0.0
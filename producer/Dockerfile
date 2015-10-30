FROM javabilities/docker-trusty
MAINTAINER Lloyd H. Meinholz <meinholz@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

# Configure the producer user
RUN echo 'root:producer' | chpasswd && \
    groupadd producer && useradd producer -s /bin/bash -m -g producer -G producer && adduser producer sudo && \
    echo 'producer:producer' | chpasswd

# Create the application directory, clone the project and build the producer app
RUN mkdir -p /app/kafka-demo && \
    chown -R producer:producer /app && \
    git clone git://github.com/javabilities/kafka-demo.git /app/kafka-demo && \
    cd /app/kafka-demo/producer && \
    . ~/.bashrc && \
    mvn -Pprod package

# Expose port 8100 from the container to the host
EXPOSE 8100

# Execute the application
CMD ["java", "-jar", "/app/kafka-demo/producer/target/producer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]

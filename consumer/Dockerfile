FROM javabilities/docker-trusty
MAINTAINER Lloyd H. Meinholz <meinholz@gmail.com>

ENV DEBIAN_FRONTEND noninteractive

# Configure the consumer user
RUN echo 'root:consumer' | chpasswd && \
    groupadd consumer && useradd consumer -s /bin/bash -m -g consumer -G consumer && adduser consumer sudo && \
    echo 'consumer:consumer' | chpasswd

# Create the application directory, clone the project and build the consumer app
RUN mkdir -p /app/kafka-demo && \
    chown -R consumer:consumer /app && \
    git clone git://github.com/javabilities/kafka-demo.git /app/kafka-demo && \
    cd /app/kafka-demo/consumer && \
    . ~/.bashrc && \
    mvn -Pprod package

# Expose port 8200 from the container to the host
EXPOSE 8200

# Execute the application
CMD ["java", "-jar", "/app/kafka-demo/consumer/target/consumer-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]

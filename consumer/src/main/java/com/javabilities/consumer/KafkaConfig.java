package com.javabilities.consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaConfig {
    @Value("${zookeeper.host}")
    private String zookeeperHost;

    @Value("${zookeeper.port}")
    private String zookeeperPort;

    @Value("${kafka.host}")
    private String kafkaHost;

    @Value("${kafka.port}")
    private String kafkaPort;

    @Value("${consumer.topic}")
    private String topic;

    KafkaConfig() {
    }

    public String getTopic() {
        return topic;
    }

    public String getBrokerAddress() {
        return kafkaHost + ":" + kafkaPort;
    }

    public String getZookeeperAddress() {
        return zookeeperHost + ":" + zookeeperPort;
    }
}

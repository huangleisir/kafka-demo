package com.javabilities.producer.config;

import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.kafka.support.KafkaProducerContext;
import org.springframework.integration.kafka.support.ProducerConfiguration;
import org.springframework.integration.kafka.support.ProducerFactoryBean;
import org.springframework.integration.kafka.support.ProducerMetadata;
import org.springframework.messaging.MessageHandler;

import java.util.Collections;
import java.util.Map;
import java.util.Properties;

@Configuration
public class KafkaConfig {
    private final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @ServiceActivator(inputChannel = "test")
    @Bean
    public MessageHandler testHandler() throws Exception {
        String topic = "test";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "signalMetadata-ingest")
    @Bean
    public MessageHandler signalMetadataHandler() throws Exception {
        String topic = "signalMetadata-ingest";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "gatewayStatusMessage-ingest")
    @Bean
    public MessageHandler gatewayStatusMessageHandler() throws Exception {
        String topic = "gatewayStatusMessage-ingest";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "uplinkPayloadMetadata-ingest")
    @Bean
    public MessageHandler uplinkPayloadMetadataHandler() throws Exception {
        String topic = "uplinkPayloadMetadata-ingest";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    @ServiceActivator(inputChannel = "downlink")
    @Bean
    public MessageHandler downlinkHandler() throws Exception {
        String topic = "downlink";
        KafkaProducerMessageHandler handler = new KafkaProducerMessageHandler(producerContext(topic));
        handler.setTopicExpression(new LiteralExpression(topic));
        return handler;
    }

    public KafkaProducerContext producerContext(String topic) throws Exception {
        KafkaProducerContext kafkaProducerContext = new KafkaProducerContext();
        ProducerMetadata<String, String> producerMetadata = new ProducerMetadata<>(topic, String.class,
                String.class, new StringSerializer(), new StringSerializer());
        Properties props = new Properties();
        props.put("linger.ms", "1000");
        ProducerFactoryBean<String, String> producer = new ProducerFactoryBean<>(producerMetadata, getBrokerAddress(), props);
        ProducerConfiguration<String, String> config = new ProducerConfiguration<>(producerMetadata, producer.getObject());
        Map<String, ProducerConfiguration<?, ?>> producerConfigurationMap =
                Collections.<String, ProducerConfiguration<?, ?>>singletonMap(topic, config);
        kafkaProducerContext.setProducerConfigurations(producerConfigurationMap);
        return kafkaProducerContext;
    }

    public String getBrokerAddress() {
        return kafkaProperties.getHost() + ":" + kafkaProperties.getPort();
    }

    public String getZookeeperConnect() {
        return zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort();
    }
}

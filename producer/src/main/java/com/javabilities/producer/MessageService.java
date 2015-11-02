package com.javabilities.producer;

import kafka.admin.AdminUtils;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.TopicMetadataResponse;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.utils.ZKStringSerializer$;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class MessageService {
    private final Logger log = LoggerFactory.getLogger(MessageService.class);

    @Inject
    private ProducerProperties producerProperties;

    public List<String> listTopics() {
        log.debug("List Topics");
        SimpleConsumer consumer = new SimpleConsumer(producerProperties.getKafka().getHost(),
                                                     producerProperties.getKafka().getPort(),
                                                     producerProperties.getKafka().getSoTimeout(),
                                                     producerProperties.getKafka().getBufferSize(),
                                                     producerProperties.getKafka().getClientId());
        List<String> topics = new ArrayList<>();
        TopicMetadataRequest request = new TopicMetadataRequest(topics);
        TopicMetadataResponse response = consumer.send(request);
        List<TopicMetadata> metadata = response.topicsMetadata();

        for (TopicMetadata item : metadata) {
            topics.add(item.topic());
        }

        log.debug("Found {} Topics", topics.size());
        return topics;
    }

    public boolean doesTopicExist(String topic) {
        log.debug("Does Topic {} exist?", topic);
        SimpleConsumer consumer = new SimpleConsumer(producerProperties.getKafka().getHost(),
                producerProperties.getKafka().getPort(),
                producerProperties.getKafka().getSoTimeout(),
                producerProperties.getKafka().getBufferSize(),
                producerProperties.getKafka().getClientId());
        List<String> topics = new ArrayList<>();
        TopicMetadataRequest request = new TopicMetadataRequest(topics);
        TopicMetadataResponse response = consumer.send(request);
        List<TopicMetadata> metadata = response.topicsMetadata();

        for (TopicMetadata item : metadata) {
            if (item.topic().equals(topic)) {
                log.debug("Found Topic {}.", topic);
                return true;
            }
        }
        log.debug("Did not find Topic {}.", topic);
        return false;
    }

    public void createTopic(String topic) {
        log.debug("Create Topic {}.", topic);
        if (doesTopicExist(topic)) {
            log.debug("Topic %s already exists.", topic);
            return;
        }
        // Create a ZooKeeper client
        ZkClient zkClient = new ZkClient(producerProperties.getZookeeper().getHost() + ":" + producerProperties.getZookeeper().getPort(),
                                         producerProperties.getZookeeper().getSessionTimeoutMs(),
                                         producerProperties.getZookeeper().getConnectionTimeoutMs(),
                                         ZKStringSerializer$.MODULE$);

        // Create the topic
        Properties topicConfig = new Properties();
        AdminUtils.createTopic(zkClient, topic, producerProperties.getKafka().getNumPartitions(),
                               producerProperties.getKafka().getReplicationFactor(), topicConfig);
        log.debug("Topic {} is created.", topic);
    }

    public void sendMessage(String topic, String message) {
        if (!doesTopicExist(topic)) {
            log.debug("Cannot send message {}. Topic {} does not exist!", message, topic);
            return;
        }

        Properties properties = new Properties();
        properties.put("metadata.broker.list", producerProperties.getKafka().getHost() + ":" + producerProperties.getKafka().getPort());
        properties.put("serializer.class", producerProperties.getKafka().getSerializerClass());
        properties.put("partitioner.class", producerProperties.getKafka().getPartitionerClass());
        properties.put("request.required.acks", producerProperties.getKafka().getRequestRequiredAcks());
        ProducerConfig config = new ProducerConfig(properties);
        Producer<String, String> producer = new Producer<>(config);
        KeyedMessage<String, String> data = new KeyedMessage<>(topic, message);
        producer.send(data);
        producer.close();
    }
}

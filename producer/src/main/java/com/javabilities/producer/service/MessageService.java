package com.javabilities.producer.service;

import com.javabilities.producer.config.KafkaProperties;
import com.javabilities.producer.config.ZookeeperProperties;
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
    private ZookeeperProperties zookeeperProperties;

    @Inject
    private KafkaProperties kafkaProperties;

    public List<String> listTopics() {
        log.debug("List Topics");
        SimpleConsumer consumer = new SimpleConsumer(kafkaProperties.getHost(),
                                                     kafkaProperties.getPort(),
                                                     kafkaProperties.getSoTimeout(),
                                                     kafkaProperties.getBufferSize(),
                                                     kafkaProperties.getClientId());
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
        SimpleConsumer consumer = new SimpleConsumer(kafkaProperties.getHost(),
                                                     kafkaProperties.getPort(),
                                                     kafkaProperties.getSoTimeout(),
                                                     kafkaProperties.getBufferSize(),
                                                     kafkaProperties.getClientId());
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
        ZkClient zkClient = new ZkClient(zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort(),
                                         zookeeperProperties.getSessionTimeoutMs(),
                                         zookeeperProperties.getConnectionTimeoutMs(),
                                         ZKStringSerializer$.MODULE$);

        // Create the topic
        Properties topicConfig = new Properties();
        AdminUtils.createTopic(zkClient, topic, kafkaProperties.getNumPartitions(), kafkaProperties.getReplicationFactor(), topicConfig);
        log.debug("Topic {} is created.", topic);
    }

    public void sendMessage(String topic, String message) {
        if (!doesTopicExist(topic)) {
            log.debug("Cannot send message {}. Topic {} does not exist!", message, topic);
            return;
        }

        Properties properties = new Properties();
        properties.put("metadata.broker.list", kafkaProperties.getHost() + ":" + kafkaProperties.getPort());
        properties.put("serializer.class", kafkaProperties.getSerializerClass());
        properties.put("partitioner.class", kafkaProperties.getPartitionerClass());
        properties.put("request.required.acks", kafkaProperties.getRequestRequiredAcks());
        ProducerConfig config = new ProducerConfig(properties);
        Producer<String, String> producer = new Producer<>(config);
        KeyedMessage<String, String> data = new KeyedMessage<>(topic, message);
        producer.send(data);
        producer.close();
    }
}

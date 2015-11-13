package com.javabilities.consumer.web;

import com.javabilities.consumer.config.KafkaProperties;
import com.javabilities.consumer.config.ZookeeperProperties;
import com.javabilities.consumer.domain.UplinkMessage;
import com.javabilities.consumer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.Message;
import org.springframework.messaging.PollableChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class ConsumerController {
    private final Logger logger = LoggerFactory.getLogger(ConsumerController.class);

    @Inject
    ConfigurableApplicationContext context;

    @Inject
    KafkaService kafkaService;




    @Autowired
    ZookeeperProperties zookeeperProperties;

    @Autowired
    KafkaProperties kafkaProperties;

    @Value("${consumer.topic}")
    private String topic;




    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        logger.debug("zookeeperHost: {}", zookeeperProperties.getHost());
        logger.debug("zookeeperPort: {}", zookeeperProperties.getPort());
        logger.debug("kafkaHost: {}", kafkaProperties.getHost());
        logger.debug("kafkaPort: {}", kafkaProperties.getPort());
        logger.debug("topic: {}", topic);

        PollableChannel fromKafka = context.getBean("received", PollableChannel.class);
        Message<?> received = fromKafka.receive(10000);
        while (received != null) {
            System.out.println(received);
            logger.debug("Headers: " + received.getHeaders());
            logger.debug("Payload: " + received.getPayload());
            received = fromKafka.receive(10000);
        }

        return "index";
    }

    @MessageMapping("/kafka")
    public UplinkMessage handle(UplinkMessage message) {
        return message;
    }
}

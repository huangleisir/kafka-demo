package com.javabilities.producer.web;

import com.javabilities.producer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class ProducerController {
    private final Logger logger = LoggerFactory.getLogger(ProducerController.class);

    @Inject
    ConfigurableApplicationContext context;

    @Inject
    KafkaService kafkaService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        logger.info("Home Controller");
        logger.info(kafkaService.getBrokerAddress());
        logger.info(kafkaService.getZookeeperConnect());

        MessageChannel toKafka = context.getBean("toKafka", MessageChannel.class);
        for (int i = 0; i < 10; i++) {
            toKafka.send(new GenericMessage<>("foo" + i));
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return "index";
    }
}

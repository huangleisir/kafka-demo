package com.javabilities.producer.web;

import com.javabilities.producer.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
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

        kafkaService.sendMessage("test", "test message");
        kafkaService.sendMessage("signalMetadata-ingest", "SignalMetadata message");
        kafkaService.sendMessage("gatewayStatusMessage-ingest", "GatewayStatusMessage message");
        kafkaService.sendMessage("uplinkPayloadMetadata-ingest", "UplinkPayloadMetadata message");
        kafkaService.sendMessage("downlink", "Downlink message");

        return "index";
    }
}

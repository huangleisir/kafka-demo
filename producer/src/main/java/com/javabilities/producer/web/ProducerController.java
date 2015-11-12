package com.javabilities.producer.web;

import com.javabilities.producer.service.MessageService;
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
    MessageService messageService;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String home() {
        logger.info("Home Controller");

        messageService.sendMessage("test", "test message");
        messageService.sendMessage("signalMetadata-ingest", "SignalMetadata message");
        messageService.sendMessage("gatewayStatusMessage-ingest", "GatewayStatusMessage message");
        messageService.sendMessage("uplinkPayloadMetadata-ingest", "UplinkPayloadMetadata message");
        messageService.sendMessage("downlink", "Downlink message");

        return "index";
    }
}

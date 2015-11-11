package com.javabilities.producer.web;

import com.javabilities.producer.domain.MessagePayload;
import com.javabilities.producer.domain.TopicPayload;
import com.javabilities.producer.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/api/v1")
public class MessageResource {
    private final Logger logger = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageService messageService;

    /**
     * GET  /topics -> get all topics.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<String>>> listTopics() {
        logger.info("REST request to get all Topics");
        List<String> topics = messageService.listTopics();
        HttpStatus status = topics != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        HashMap<String, List<String>> topicJson = new HashMap<>();
        topicJson.put("topics", topics);
        return new ResponseEntity<>(topicJson, status);
    }

    /**
     * POST  /topics -> create a topic.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createTopic(@RequestBody TopicPayload topicPayload) {
        String topic = topicPayload.getTopic();
        logger.info("REST request to create a Topic: {}", topic);
        messageService.createTopic(topic);
        HashMap<String, String> topicJson = new HashMap<>();
        topicJson.put("topic", topic);
        return new ResponseEntity<>(topicJson, HttpStatus.OK);
    }

    /**
     * POST  /messages -> send a message.
     */
    @RequestMapping(value = "/messages",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody MessagePayload messagePayload) {
        String topic = messagePayload.getTopic();
        String message = messagePayload.getMessage();
        logger.info("REST request to send a Message {} to a Topic {}", message, topic);
        messageService.sendMessage(topic, message);
        HashMap<String, String> topicJson = new HashMap<>();
        topicJson.put("status", "success");
        return new ResponseEntity<>(topicJson, HttpStatus.OK);
    }
}

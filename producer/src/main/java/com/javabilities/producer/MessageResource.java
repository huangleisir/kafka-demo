package com.javabilities.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/api/1")
public class MessageResource {
    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    @Inject
    private MessageService messageService;

    /**
     * GET  /topics -> get all topics.
     */
    @RequestMapping(value = "/topics",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<String>>> listTopics() {
        log.debug("REST request to get all Topics");
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
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createTopic(@RequestParam(value="topic") String topic) {
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
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> sendMessage(@RequestParam(value="topic") String topic, @RequestParam(value="message") String message) {
        messageService.sendMessage(topic, message);
        HashMap<String, String> topicJson = new HashMap<>();
        topicJson.put("status", "success");
        return new ResponseEntity<>(topicJson, HttpStatus.OK);
    }
}

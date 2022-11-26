package org.home.knowledge.sow.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.Topic;
import org.home.knowledge.sow.model.dto.TopicSummary;
import org.home.knowledge.sow.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/topics")
@Slf4j
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    // create
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<Topic> save(@RequestBody Topic topic) {
        log.info("Saving topic: {}", topic);
        return ResponseEntity.ok(topicService.save(topic));
    }

    // read
    @GetMapping
    public ResponseEntity<List<TopicSummary>> findAllSummaries(@RequestParam(required = false) String q) {
        if (StringUtils.isNotBlank(q)) {
            log.info("Retrieving all topic summaries which contain: {}", q);
            return ResponseEntity.ok(topicService.findSummariesByNameContaining(q));
        } else {
            log.info("Retrieving all topic summaries");
            return ResponseEntity.ok(topicService.findAllSummaries());
        }
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Topic> findById(@PathVariable Long id) {
        log.info("Retrieving topic by id: {}", id);
        return ResponseEntity.ok(topicService.findById(id));
    }

    // update
    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Topic> update(@PathVariable Long id, @RequestBody Topic topic) {
        log.info("Updating topic: {}", topic);
        return ResponseEntity.ok(topicService.save(topic));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Deleting topic by id: {}", id);
        return ResponseEntity.ok(topicService.deleteById(id));
    }

}

package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Topic;
import org.home.knowledge.sow.model.dto.TopicSummary;
import org.home.knowledge.sow.repository.TopicRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TopicService {

    private final TopicRepository topicRepository;

    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    // create
    public Topic save(Topic topic) {
        log.trace("Saving topic: {}", topic);
        return topicRepository.save(topic);
    }

    public List<Topic> saveAll(List<Topic> topics) {
        log.trace("Saving list of topics");
        return topicRepository.saveAll(topics);
    }

    // read
    public List<TopicSummary> findSummariesByNameContaining(String q) {
        log.trace("Retrieving all topic summaries which contain: {}", q);
        return topicRepository.findTopicSummariesByNameContainingIgnoreCase(q);
    }

    public List<TopicSummary> findAllSummaries() {
        log.trace("Retrieving all topic summaries");
        return topicRepository.findAllProjectedBy();
    }

    public Topic findById(Long id) {
        log.trace("Retrieving topic by id: {}", id);
        return topicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find topic by id: " + id));
    }

    // update
    public Topic update(Topic topic) {
        log.trace("Updating topic: {}", topic);
        return topicRepository.save(topic);
    }

    // delete
    public boolean deleteById(Long id) {
        log.trace("Deleting topic by id: {}", id);
        topicRepository.deleteById(id);
        return true;
    }

}

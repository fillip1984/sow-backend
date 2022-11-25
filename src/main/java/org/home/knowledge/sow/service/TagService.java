package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.model.dto.TagSummary;
import org.home.knowledge.sow.repository.TagRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TagService {

    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // create
    public Tag save(Tag tag) {
        log.info("Saving tag: {}", tag);
        return tagRepository.save(tag);
    }

    public List<Tag> saveAll(List<Tag> tags) {
        log.info("Saving list of tags");
        return tagRepository.saveAll(tags);
    }

    // read
    public List<TagSummary> findSummariesByNameContaining(String q) {
        log.info("Retrieving all tag summaries which contain: {}", q);
        return tagRepository.findTagSummariesByNameContainingIgnoreCase(q);
    }

    public List<TagSummary> findAllSummaries() {
        log.info("Retrieving all tag summaries");
        return tagRepository.findAllProjectedBy();
    }

    public Tag findById(Long id) {
        log.info("Retrieving tag by id: {}", id);
        return tagRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find tag by id: " + id));
    }

    // update
    public Tag update(Tag tag) {
        log.info("Updating tag: {}", tag);
        return tagRepository.save(tag);
    }

    // delete
    public boolean deleteById(Long id) {
        log.info("Deleting tag by id: {}", id);
        tagRepository.deleteById(id);
        return true;
    }
}

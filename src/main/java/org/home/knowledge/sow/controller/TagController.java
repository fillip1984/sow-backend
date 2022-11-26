package org.home.knowledge.sow.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.model.dto.TagSummary;
import org.home.knowledge.sow.service.TagService;
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
@RequestMapping("/api/v1/tags")
@Slf4j
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // create
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<Tag> save(@RequestBody Tag tag) {
        log.info("Saving tag: {}", tag);
        return ResponseEntity.ok(tagService.save(tag));
    }

    // read
    @GetMapping
    public ResponseEntity<List<TagSummary>> findAllSummaries(@RequestParam(required = false) String q) {
        if (StringUtils.isNotBlank(q)) {
            log.info("Retrieving all tag summaries which contain: {}", q);
            return ResponseEntity.ok(tagService.findSummariesByNameContaining(q));
        } else {
            log.info("Retrieving all tag summaries");
            return ResponseEntity.ok(tagService.findAllSummaries());
        }
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Tag> findById(@PathVariable Long id) {
        log.info("Retrieving tag by id: {}", id);
        return ResponseEntity.ok(tagService.findById(id));
    }

    // update
    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Tag> update(@PathVariable Long id, @RequestBody Tag tag) {
        log.info("Updating tag: {}", tag);

        var existingTag = tagService.findById(id);
        existingTag.setName(tag.getName());
        existingTag.setDescription(tag.getDescription());

        return ResponseEntity.ok(tagService.save(existingTag));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Deleting tag by id: {}", id);
        return ResponseEntity.ok(tagService.deleteById(id));
    }

}

package org.home.knowledge.sow.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/tags")
@Slf4j
public class TagController {

    @Autowired
    private TagService tagService;

    // create
    @PostMapping
    public ResponseEntity<Tag> save(@RequestBody Tag tag) {
        log.info("Saving tag: {}", tag);
        return ResponseEntity.ok(tagService.save(tag));
    }

    // read
    @GetMapping
    public ResponseEntity<List<Tag>> findAll(@RequestParam(required = false) String query) {
        if (StringUtils.isBlank(query)) {
            log.info("Retrieving all tags");
            return ResponseEntity.ok(tagService.findAll());
        } else {
            log.info("Searching for all tags containing: {}", query);
            return ResponseEntity.ok(tagService.search(query));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> findById(@PathVariable Long id) {
        log.info("Retrieving tag by id: {}", id);
        return ResponseEntity.ok(tagService.findById(id));
    }

    // update
    @PutMapping("/{id}")
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

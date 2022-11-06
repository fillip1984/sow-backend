package org.home.knowledge.sow.controller;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.service.AuthorService;
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
@RequestMapping("/api/v1/authors")
@Slf4j
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // create
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<Author> save(@RequestBody Author author) {
        log.info("Saving author: {}", author);
        return ResponseEntity.ok(authorService.save(author));
    }

    // read
    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Author>> findAll(@RequestParam(required = false) String q) {
        if (StringUtils.isNotBlank(q)) {
            log.info("Retrieving all authors which contain: {}", q);
            return ResponseEntity.ok(authorService.findByNameContaining(q));
        } else {

            log.info("Retrieving all authors");
            return ResponseEntity.ok(authorService.findAll());
        }
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Author> findById(@PathVariable Long id) {
        log.info("Retrieving author by id: {}", id);
        return ResponseEntity.ok(authorService.findById(id));
    }

    // update
    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Author> update(@PathVariable Long id, @RequestBody Author author) {
        log.info("Updating author: {}", author);

        var existingAuthor = authorService.findById(id);
        existingAuthor.setFirstName(author.getFirstName());
        existingAuthor.setLastName(author.getLastName());
        existingAuthor.setPreferredName(author.getPreferredName());
        existingAuthor.setBio(author.getBio());

        return ResponseEntity.ok(authorService.save(existingAuthor));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Deleting author by id: {}", id);
        return ResponseEntity.ok(authorService.deleteById(id));
    }
}

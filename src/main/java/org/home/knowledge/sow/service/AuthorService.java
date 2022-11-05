package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    // create
    public Author save(Author author) {
        log.info("Saving author: {}", author);
        return authorRepository.save(author);
    }

    public List<Author> saveAll(List<Author> authors) {
        log.info("Saving list of authors");
        return authorRepository.saveAll(authors);
    }

    // read
    public List<Author> findAll() {
        log.info("Retrieving all authors");
        return authorRepository.findAll();
    }

    public Author findById(Long id) {
        log.info("Retrieving author by id: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find author by id: " + id));
    }

    public List<Author> findByNameContaining(String q) {
        log.info("Retrieving all posts which contain: {}", q);
        return authorRepository.findByNameContainingIgnoreCase(q);
    }

    // update
    public Author update(Author author) {
        log.info("Updating author: {}", author);
        return authorRepository.save(author);
    }

    // delete
    public boolean deleteById(Long id) {
        log.info("Deleting author by id: {}", id);
        authorRepository.deleteById(id);
        return true;
    }
}
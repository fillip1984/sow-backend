package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.dto.AuthorSummary;
import org.home.knowledge.sow.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // create
    public Author save(Author author) {
        log.trace("Saving author: {}", author);
        return authorRepository.save(author);
    }

    public List<Author> saveAll(List<Author> authors) {
        log.trace("Saving list of authors");
        return authorRepository.saveAll(authors);
    }

    // read
    public List<AuthorSummary> findSummariesByNameContaining(String q) {
        log.trace("Retrieving all author summaries which contain: {}", q);
        return authorRepository.findAuthorSummariesByNamesContainingIgnoreCase(q);
    }

    public List<AuthorSummary> findAllSummaries() {
        log.trace("Retrieving all author summaries");
        return authorRepository.findAllProjectedBy();
    }

    // public List<Author> findAll() {
    // log.trace("Retrieving all authors");
    // return authorRepository.findAll();
    // }

    // public List<Author> findByNameContaining(String q) {
    // log.trace("Retrieving all authors which contain: {}", q);
    // return authorRepository.findByNameContainingIgnoreCase(q);
    // }

    public Author findById(Long id) {
        log.trace("Retrieving author by id: {}", id);
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find author by id: " + id));
    }

    public AuthorSummary findSummaryById(Long id) {
        log.trace("Retrieving author summary by id: {}", id);
        return authorRepository.findAuthorSummaryById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find author summary by id: " + id));
    }

    // public List<Author> findByNameContaining(String q) {
    // log.trace("Retrieving all posts which contain: {}", q);
    // return authorRepository.findByNameContainingIgnoreCase(q);
    // }

    // update
    public Author update(Author author) {
        log.trace("Updating author: {}", author);
        return authorRepository.save(author);
    }

    // delete
    public boolean deleteById(Long id) {
        log.trace("Deleting author by id: {}", id);
        authorRepository.deleteById(id);
        return true;
    }
}

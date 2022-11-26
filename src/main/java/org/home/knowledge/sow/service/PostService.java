package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.dto.PostSummary;
import org.home.knowledge.sow.repository.PostRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    // create
    public Post save(Post post) {
        log.trace("Saving post: {}", post);
        return postRepository.save(post);
    }

    public List<Post> saveAll(List<Post> posts) {
        log.trace("Saving list of posts");
        return postRepository.saveAll(posts);
    }

    // read
    public List<PostSummary> findSummariesByTitleContaining(String q) {
        log.trace("Retrieving all post summaries which contain: {}", q);
        return postRepository.findPostSummariesByTitleContainingIgnoreCase(q);
    }

    public List<PostSummary> findAllSummaries() {
        log.trace("Retrieving all post summaries");
        return postRepository.findAllProjectedBy();
    }

    // public List<Post> findAll() {
    // log.trace("Retrieving all posts");
    // return postRepository.findAll();
    // }

    // public List<Post> findByTitleContaining(String q) {
    // log.trace("Retrieving all posts which contain: {}", q);
    // return postRepository.findPostSummariesByTitleContainingIgnoreCase(q);
    // }

    public Post findById(Long id) {
        log.trace("Retrieving post by id: {}", id);
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find post by id: " + id));
    }

    // update
    public Post update(Post post) {
        log.trace("Updating post: {}", post);
        return postRepository.save(post);
    }

    // delete
    public boolean deleteById(Long id) {
        log.trace("Deleting post by id: {}", id);
        postRepository.deleteById(id);
        return true;
    }
}

package org.home.knowledge.sow.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.home.knowledge.sow.json.Views;
import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.dto.PostSummary;
import org.home.knowledge.sow.service.PostService;
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
@RequestMapping("/api/v1/posts")
@Slf4j
public class PostController {

    private final PostService postService;
    private final TagService tagService;

    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    // create
    @PostMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<Post> save(@RequestBody Post post) {
        log.info("Saving post: {}", post);
        return ResponseEntity.ok(postService.save(post));
    }

    // read
    @GetMapping
    public ResponseEntity<List<PostSummary>> findAllSummaries(@RequestParam(required = false) String q) {
        if (StringUtils.isNotBlank(q)) {
            log.info("Retrieving all post summaries which contain: {}", q);
            return ResponseEntity.ok(postService.findSummariesByTitleContaining(q));
        } else {
            log.info("Retrieving all post summaries");
            return ResponseEntity.ok(postService.findAllSummaries());
        }
    }

    @GetMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        log.info("Retrieving post by id: {}", id);
        return ResponseEntity.ok(postService.findById(id));
    }

    // update
    @PutMapping("/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Post> update(@PathVariable Long id, @RequestBody Post post) {
        log.info("Updating post: {}", post);

        var existingPost = postService.findById(id);
        existingPost.setTitle(post.getTitle());
        existingPost.setShortDescription(post.getShortDescription());
        existingPost.setContents(post.getContents());
        existingPost.setAuthor(post.getAuthor());
        existingPost.setTopic(post.getTopic());
        existingPost.setTags(post.getTags());

        return ResponseEntity.ok(postService.save(existingPost));
    }

    // delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Long id) {
        log.info("Deleting post by id: {}", id);
        return ResponseEntity.ok(postService.deleteById(id));
    }

    // tag endpoints
    @PutMapping("/{postId}/tags/{tagId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Boolean> addTagById(@PathVariable Long postId, @PathVariable Long tagId) {
        log.info("Adding tag with id: {} to post with id: {}", tagId, postId);
        var tag = tagService.findById(tagId);
        var post = postService.findById(postId);
        post.getTags().add(tag);
        postService.save(post);
        return ResponseEntity.ok(true);
    }

    @DeleteMapping("/{postId}/tags/{tagId}")
    public ResponseEntity<Boolean> deleteTagById(@PathVariable Long postId, @PathVariable Long tagId) {
        log.info("Deleting tag with id: {} from post with id: {}", tagId, postId);
        var post = postService.findById(postId);
        post.setTags(
                post.getTags().stream().filter(tag -> tag.getId().equals(tagId) == false).collect(Collectors.toList()));
        postService.save(post);
        return ResponseEntity.ok(true);
    }

}

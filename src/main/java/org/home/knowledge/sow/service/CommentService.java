package org.home.knowledge.sow.service;

import java.util.List;

import org.home.knowledge.sow.model.Comment;
import org.home.knowledge.sow.model.dto.CommentSummary;
import org.home.knowledge.sow.repository.CommentRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // create
    public Comment save(Comment comment) {
        log.info("Saving comment: {}", comment);
        return commentRepository.save(comment);
    }

    public List<Comment> saveAll(List<Comment> comments) {
        log.info("Saving list of comments");
        return commentRepository.saveAll(comments);
    }

    // read
    public List<CommentSummary> findAllSummaries() {
        log.info("Retrieving all comment summaries");
        return commentRepository.findAllProjectedBy();
    }

    public Comment findById(Long id) {
        log.info("Retrieving comment by id: {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unable to find comment by id: " + id));
    }

    // update
    public Comment update(Comment comment) {
        log.info("Updating comment: {}", comment);
        return commentRepository.save(comment);
    }

    // delete
    public boolean deleteById(Long id) {
        log.info("Deleting comment by id: {}", id);
        commentRepository.deleteById(id);
        return true;
    }

}

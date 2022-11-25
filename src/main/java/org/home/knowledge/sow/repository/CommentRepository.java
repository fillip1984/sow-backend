package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Comment;
import org.home.knowledge.sow.model.dto.CommentSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<CommentSummary> findAllProjectedBy();

}

package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Post;
import org.home.knowledge.sow.model.dto.PostSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public List<PostSummary> findPostSummaryByTitleContainingIgnoreCase(String q);

    public List<Post> findByTitleContainingIgnoreCase(String q);

    public List<PostSummary> findAllProjectedBy();

    public List<Post> findAll();
}

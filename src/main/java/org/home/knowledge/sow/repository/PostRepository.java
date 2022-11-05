package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    public List<Post> findByTitleContainingIgnoreCase(String title);
}

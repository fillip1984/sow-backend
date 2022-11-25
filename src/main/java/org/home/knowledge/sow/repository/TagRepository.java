package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Tag;
import org.home.knowledge.sow.model.dto.TagSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<TagSummary> findTagSummariesByNameContainingIgnoreCase(String name);

    List<TagSummary> findAllProjectedBy();

}

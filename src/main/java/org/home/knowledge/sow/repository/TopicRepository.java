package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Topic;
import org.home.knowledge.sow.model.dto.TopicSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {

    List<TopicSummary> findTopicSummariesByNameContainingIgnoreCase(String name);

    List<TopicSummary> findAllProjectedBy();

}

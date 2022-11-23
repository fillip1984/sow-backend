package org.home.knowledge.sow.model.dto;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public interface PostSummary extends AbstractSummary {

    Long getId();

    String getTitle();

    String getShortDescription();

    AuthorSummary getAuthor();

    TopicSummary getTopic();

    @Value("#{target.comments.size()}")
    int getCommentCount();

    List<TagSummary> getTags();
}

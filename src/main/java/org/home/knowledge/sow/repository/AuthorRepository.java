package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.dto.AuthorSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // https://stackoverflow.com/questions/40356565/spring-data-crudrepository-query-with-like-and-ignorecase
    @Query("SELECT a FROM Author a WHERE firstName like :name or lastName like :name or preferredName like :name")
    List<AuthorSummary> findAuthorSummariesByNameContainingIgnoreCase(String name);

    List<AuthorSummary> findAllProjectedBy();

}

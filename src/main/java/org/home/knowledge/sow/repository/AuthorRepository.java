package org.home.knowledge.sow.repository;

import java.util.List;
import java.util.Optional;

import org.home.knowledge.sow.model.Author;
import org.home.knowledge.sow.model.dto.AuthorSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // https://stackoverflow.com/questions/40356565/spring-data-crudrepository-query-with-like-and-ignorecase
    @Query("SELECT a FROM Author a WHERE UPPER(firstName) like UPPER(concat(concat('%', :name), '%')) or UPPER(lastName) like UPPER(concat(concat('%', :name), '%')) or UPPER(preferredName) like UPPER(concat(concat('%', :name), '%'))")
    List<AuthorSummary> findAuthorSummariesByNamesContainingIgnoreCase(String name);

    List<AuthorSummary> findAllProjectedBy();

    Optional<AuthorSummary> findAuthorSummaryById(Long id);

}

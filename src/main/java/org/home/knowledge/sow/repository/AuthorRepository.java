package org.home.knowledge.sow.repository;

import java.util.List;

import org.home.knowledge.sow.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    public List<Author> findByNameContainingIgnoreCase(String name);
}

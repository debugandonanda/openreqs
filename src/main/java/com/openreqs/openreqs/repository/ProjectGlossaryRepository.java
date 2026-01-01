package com.openreqs.openreqs.repository;

import com.openreqs.openreqs.domain.entity.ProjectGlossary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectGlossaryRepository extends JpaRepository<ProjectGlossary, Long> {
    Optional<ProjectGlossary> findByTerm(String term);
}
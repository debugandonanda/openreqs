package com.openreqs.openreqs.repository;

import com.openreqs.openreqs.domain.entity.Requirement;
import com.openreqs.openreqs.domain.enums.RequirementStatus;
import com.openreqs.openreqs.domain.enums.RequirementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Long> {

    Optional<Requirement> findByRequirementId(String requirementId);

    List<Requirement> findByStatus(RequirementStatus status);

    List<Requirement> findByType(RequirementType type);

    List<Requirement> findBySourceId(Long sourceId);

    List<Requirement> findByIsArchivedFalse();

    @Query("SELECT r FROM Requirement r WHERE " +
            "LOWER(r.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(r.rationale) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Requirement> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT r FROM Requirement r WHERE r.version = :version")
    List<Requirement> findByVersion(@Param("version") String version);
}
package com.openreqs.openreqs.repository;

import com.openreqs.openreqs.domain.entity.Stakeholder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StakeholderRepository extends JpaRepository<Stakeholder, Long> {
    Optional<Stakeholder> findByEmail(String email);
    Optional<Stakeholder> findByName(String name);
}
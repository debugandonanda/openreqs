package com.openreqs.openreqs.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * IREB Syllabus - Unidade 1: Glossário
 * Dicionário de termos do projeto para garantir entendimento comum
 */
@Entity
@Table(name = "project_glossary")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectGlossary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String term;

    @Column(length = 2000)
    private String definition;

    @Column
    private String acronym;

    @Column(name = "related_terms")
    private String relatedTerms;

    @Column(name = "business_domain")
    private String businessDomain;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }
}
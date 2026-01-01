package com.openreqs.openreqs.domain.entity;

import com.openreqs.openreqs.domain.enums.RequirementStatus;
import com.openreqs.openreqs.domain.enums.RequirementType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * IREB Syllabus - Entidade central do sistema
 * Representa um requisito com todos os atributos recomendados pelo IREB
 */
@Entity
@Table(name = "requirements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Requirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "requirement_id", nullable = false, unique = true)
    private String requirementId; // Formato: REQ-001, REQ-002

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(length = 3000)
    @Builder.Default
    private String rationale = "";

    @Column(name = "fit_criterion", length = 2000)
    @Builder.Default
    private String fitCriterion = "";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequirementType type = RequirementType.FUNCTIONAL;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequirementStatus status = RequirementStatus.DRAFT;

    @Column(name = "priority")
    @Builder.Default
    private Integer priority = 2; // 1-Alta, 2-Média, 3-Baixa

    @Column(name = "version")
    @Builder.Default
    private String version = "v1.0";

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_updated")
    @Builder.Default
    private LocalDateTime lastUpdated = LocalDateTime.now();

    @Column(name = "is_archived")
    @Builder.Default
    private Boolean isArchived = false;

    // Relacionamentos
    @ManyToOne
    @JoinColumn(name = "source_id")
    private Stakeholder source;

    @ManyToMany
    @JoinTable(
            name = "requirement_dependencies",
            joinColumns = @JoinColumn(name = "requirement_id"),
            inverseJoinColumns = @JoinColumn(name = "depends_on_id")
    )
    @Builder.Default
    private List<Requirement> dependencies = new ArrayList<>();

    @ManyToMany(mappedBy = "dependencies")
    @Builder.Default
    private List<Requirement> dependentOn = new ArrayList<>();

    // Métodos auxiliares
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        lastUpdated = LocalDateTime.now();
    }

    public void archive() {
        this.isArchived = true;
        this.status = RequirementStatus.DEPRECATED;
    }

    public Requirement createNewVersion() {
        Requirement newVersion = Requirement.builder()
                .requirementId(this.requirementId)
                .description(this.description)
                .rationale(this.rationale)
                .fitCriterion(this.fitCriterion)
                .type(this.type)
                .status(RequirementStatus.DRAFT)
                .priority(this.priority)
                .source(this.source)
                .dependencies(new ArrayList<>(this.dependencies))
                .build();

        // Incrementa versão
        String currentVersion = this.version.replace("v", "");
        double versionNum = Double.parseDouble(currentVersion);
        newVersion.setVersion(String.format("v%.1f", versionNum + 0.1));

        return newVersion;
    }
}
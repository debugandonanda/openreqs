package com.openreqs.openreqs.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * IREB Syllabus - Unidade 3: Análise de Stakeholders
 * Representa uma fonte ou interessado no sistema
 */
@Entity
@Table(name = "stakeholders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stakeholder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String role; // Ex: Product Owner, Usuário Final, Desenvolvedor

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 1000)
    @Builder.Default
    private String responsibilities = "";

    @Column(name = "influence_level")
    @Builder.Default
    private String influenceLevel = "Médio";

    @OneToMany(mappedBy = "source", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Requirement> requirements = new ArrayList<>();
}
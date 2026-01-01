package com.openreqs.openreqs.dto;

import com.openreqs.openreqs.domain.enums.RequirementStatus;
import com.openreqs.openreqs.domain.enums.RequirementType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para resposta de requisitos")
public class RequirementResponse {

    @Schema(description = "ID do banco de dados")
    private Long id;

    @Schema(description = "Identificador único do requisito")
    private String requirementId;

    @Schema(description = "Descrição do requisito")
    private String description;

    @Schema(description = "Justificativa")
    private String rationale;

    @Schema(description = "Critério de aceitação")
    private String fitCriterion;

    @Schema(description = "Tipo do requisito")
    private RequirementType type;

    @Schema(description = "Status atual")
    private RequirementStatus status;

    @Schema(description = "Versão atual")
    private String version;

    @Schema(description = "Prioridade")
    private Integer priority;

    @Schema(description = "Stakeholder fonte")
    private StakeholderResponse source;

    @Schema(description = "Data de criação")
    private LocalDateTime createdAt;

    @Schema(description = "Última atualização")
    private LocalDateTime lastUpdated;

    @Schema(description = "Requisitos dependentes")
    private List<RequirementResponse> dependencies;

    @Schema(description = "Está arquivado?")
    private Boolean isArchived;
}
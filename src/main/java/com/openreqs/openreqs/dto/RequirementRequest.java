package com.openreqs.openreqs.dto;

import com.openreqs.openreqs.domain.enums.RequirementType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para criação/atualização de requisitos")
public class RequirementRequest {

    @NotBlank(message = "O ID do requisito é obrigatório")
    @Schema(description = "Identificador único do requisito (ex: REQ-001)", example = "REQ-001")
    private String requirementId;

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 2000, message = "A descrição deve ter entre 10 e 2000 caracteres")
    @Schema(description = "Descrição detalhada do requisito", example = "O sistema deve permitir login com email e senha")
    private String description;

    @NotBlank(message = "A justificativa é obrigatória")
    @Size(min = 10, max = 3000, message = "A justificativa deve ter entre 10 e 3000 caracteres")
    @Schema(description = "Justificativa do requisito", example = "Garantir segurança e autenticação dos usuários")
    private String rationale;

    @Size(max = 2000, message = "Critério de aceitação máximo 2000 caracteres")
    @Schema(description = "Critério de aceitação", example = "Usuário acessa sistema com credenciais válidas")
    private String fitCriterion;

    @Schema(description = "Tipo do requisito", example = "FUNCTIONAL")
    private RequirementType type;

    @Schema(description = "ID do stakeholder fonte", example = "1")
    private Long sourceId;

    @Schema(description = "Prioridade (1-Alta, 2-Média, 3-Baixa)", example = "1")
    private Integer priority;
}
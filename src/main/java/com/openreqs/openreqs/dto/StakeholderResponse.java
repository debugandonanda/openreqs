package com.openreqs.openreqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para resposta de stakeholders")
public class StakeholderResponse {

    @Schema(description = "ID do stakeholder")
    private Long id;

    @Schema(description = "Nome")
    private String name;

    @Schema(description = "Papel")
    private String role;

    @Schema(description = "Email")
    private String email;

    @Schema(description = "Responsabilidades")
    private String responsibilities;

    @Schema(description = "Nível de influência")
    private String influenceLevel;

    @Schema(description = "Quantidade de requisitos")
    private Integer requirementCount;
}
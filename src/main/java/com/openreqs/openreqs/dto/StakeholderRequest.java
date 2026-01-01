package com.openreqs.openreqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para criação/atualização de stakeholders")
public class StakeholderRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Schema(description = "Nome completo", example = "João Silva")
    private String name;

    @NotBlank(message = "Papel é obrigatório")
    @Schema(description = "Papel no projeto", example = "Product Owner")
    private String role;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Schema(description = "Email de contato", example = "joao@empresa.com")
    private String email;

    @Schema(description = "Responsabilidades", example = "Define backlog e prioridades")
    private String responsibilities;

    @Schema(description = "Nível de influência", example = "Alto")
    private String influenceLevel;
}
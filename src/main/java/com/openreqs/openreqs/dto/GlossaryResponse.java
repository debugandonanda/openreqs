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
@Schema(description = "DTO para resposta de termos do glossário")
public class GlossaryResponse {

    @Schema(description = "ID do termo")
    private Long id;

    @Schema(description = "Termo")
    private String term;

    @Schema(description = "Definição")
    private String definition;

    @Schema(description = "Sigla")
    private String acronym;

    @Schema(description = "Termos relacionados")
    private String relatedTerms;

    @Schema(description = "Domínio de negócio")
    private String businessDomain;

    @Schema(description = "Data de criação")
    private String createdAt;

    @Schema(description = "Última atualização")
    private String lastUpdated;
}
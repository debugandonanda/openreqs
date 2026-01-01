package com.openreqs.openreqs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO para criação/atualização de termos do glossário")
public class GlossaryRequest {

    @NotBlank(message = "Termo é obrigatório")
    @Schema(description = "Termo técnico ou de negócio", example = "API")
    private String term;

    @NotBlank(message = "Definição é obrigatória")
    @Schema(description = "Definição clara do termo",
            example = "Application Programming Interface - Interface para comunicação entre sistemas")
    private String definition;

    @Schema(description = "Sigla do termo", example = "API")
    private String acronym;

    @Schema(description = "Termos relacionados", example = "Web Service, REST, SOAP")
    private String relatedTerms;

    @Schema(description = "Domínio de negócio", example = "Tecnologia")
    private String businessDomain;
}
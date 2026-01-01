package com.openreqs.openreqs.domain.enums;

/**
 * IREB Syllabus - Tipos de Requisitos
 *
 * Requisito Funcional: Descreve o QUE o sistema deve fazer
 * Requisito de Qualidade: Descreve COMO o sistema deve fazer (desempenho, segurança)
 * Restrição: Limitações no desenvolvimento ou no sistema
 */
public enum RequirementType {
    FUNCTIONAL("Funcional",
            "Descreve funcionalidades específicas que o sistema deve executar. " +
                    "Ex: 'O sistema deve permitir login com email e senha'"),

    QUALITY("Qualidade",
            "Define atributos de qualidade como desempenho, segurança, usabilidade. " +
                    "Ex: 'O sistema deve responder em menos de 2 segundos'"),

    CONSTRAINT("Restrição",
            "Limitações ou diretrizes que afetam o desenvolvimento. " +
                    "Ex: 'O sistema deve usar Java 17'");

    private final String description;
    private final String explanation;

    RequirementType(String description, String explanation) {
        this.description = description;
        this.explanation = explanation;
    }

    public String getDescription() { return description; }
    public String getExplanation() { return explanation; }
}
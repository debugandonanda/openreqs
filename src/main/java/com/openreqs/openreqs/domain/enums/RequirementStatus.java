package com.openreqs.openreqs.domain.enums;

/**
 * IREB Syllabus - Unidade 8: Gestão de Requisitos
 * Ciclo de vida de um requisito com máquina de estados
 */
public enum RequirementStatus {
    DRAFT("Rascunho", "Requisito em elaboração inicial"),
    REVIEW("Revisão", "Em análise pelos stakeholders"),
    APPROVED("Aprovado", "Validado e pronto para implementação"),
    REJECTED("Rejeitado", "Não será implementado"),
    IMPLEMENTED("Implementado", "Desenvolvido no sistema"),
    TESTED("Testado", "Validado através de testes"),
    DEPRECATED("Obsoleto", "Substituído por nova versão");

    private final String description;
    private final String meaning;

    RequirementStatus(String description, String meaning) {
        this.description = description;
        this.meaning = meaning;
    }

    public String getDescription() { return description; }
    public String getMeaning() { return meaning; }

    public boolean canChangeTo(RequirementStatus newStatus) {
        // Máquina de estados simples conforme IREB
        return switch (this) {
            case DRAFT -> newStatus == REVIEW || newStatus == REJECTED;
            case REVIEW -> newStatus == APPROVED || newStatus == DRAFT;
            case APPROVED -> newStatus == IMPLEMENTED || newStatus == DEPRECATED;
            case IMPLEMENTED -> newStatus == TESTED;
            case TESTED -> newStatus == DEPRECATED;
            default -> false;
        };
    }
}
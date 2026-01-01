package com.openreqs.openreqs.exception;

import lombok.Getter;

/**
 * IREB Syllabus - Unidade 5: Qualidade dos Requisitos
 * Exceção lançada quando um requisito viola regras de qualidade
 */
@Getter
public class RequirementViolationException extends RuntimeException {

    private final String violationType;
    private final String problematicContent;
    private final String suggestion;

    public RequirementViolationException(String message, String violationType,
                                         String problematicContent, String suggestion) {
        super(message);
        this.violationType = violationType;
        this.problematicContent = problematicContent;
        this.suggestion = suggestion;
    }

    public RequirementViolationException(String message, String violationType) {
        this(message, violationType, null, null);
    }
}
package com.openreqs.openreqs.service.quality;

import com.openreqs.openreqs.dto.RequirementRequest;
import com.openreqs.openreqs.exception.RequirementViolationException;

/**
 * IREB Syllabus - Unidade 5: Padrões de Qualidade
 * Interface para todas as regras de validação de requisitos
 */
public interface QualityRule {
    void validate(RequirementRequest request) throws RequirementViolationException;
    String getRuleName();
    String getRuleDescription();
}
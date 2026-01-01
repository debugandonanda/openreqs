package com.openreqs.openreqs.service.quality;

import com.openreqs.openreqs.dto.RequirementRequest;
import com.openreqs.openreqs.exception.RequirementViolationException;
import org.springframework.stereotype.Component;

/**
 * IREB Syllabus - Unidade 5.3: Atributos de Requisitos
 * Verifica se o requisito está completo com todos os atributos necessários
 */
@Component
public class CompletenessChecker implements QualityRule {

    @Override
    public void validate(RequirementRequest request) {
        checkIdFormat(request.getRequirementId());
        checkDescriptionLength(request.getDescription());
        checkRationale(request.getRationale());
        checkFitCriterion(request.getFitCriterion());
    }

    private void checkIdFormat(String requirementId) {
        if (!requirementId.matches("^REQ-\\d{3}$")) {
            throw new RequirementViolationException(
                    "Formato de ID inválido",
                    "ID_FORMAT",
                    requirementId,
                    "Use o formato: REQ-001, REQ-002, etc."
            );
        }
    }

    private void checkDescriptionLength(String description) {
        if (description.length() < 10) {
            throw new RequirementViolationException(
                    "Descrição muito curta",
                    "DESCRIPTION_LENGTH",
                    description,
                    "A descrição deve ter pelo menos 10 caracteres"
            );
        }

        if (description.length() > 2000) {
            throw new RequirementViolationException(
                    "Descrição muito longa",
                    "DESCRIPTION_LENGTH",
                    String.valueOf(description.length()),
                    "Limite a descrição a 2000 caracteres"
            );
        }
    }

    private void checkRationale(String rationale) {
        if (rationale == null || rationale.trim().length() < 10) {
            throw new RequirementViolationException(
                    "Justificativa insuficiente",
                    "RATIONALE_INCOMPLETE",
                    rationale,
                    "Explique POR QUE este requisito é necessário (mínimo 10 caracteres)"
            );
        }
    }

    private void checkFitCriterion(String fitCriterion) {
        if (fitCriterion != null && fitCriterion.trim().length() > 0) {
            // Verifica se o critério de aceitação é testável
            String lowerCriterion = fitCriterion.toLowerCase();
            boolean isTestable = lowerCriterion.contains("deve") ||
                    lowerCriterion.contains("pode") ||
                    lowerCriterion.contains("verificar") ||
                    lowerCriterion.contains("validar") ||
                    lowerCriterion.matches(".*\\d+.*"); // Contém números

            if (!isTestable) {
                throw new RequirementViolationException(
                        "Critério de aceitação não testável",
                        "FIT_CRITERION_NOT_TESTABLE",
                        fitCriterion,
                        "Especifique como validar o requisito. Ex: 'Tempo de resposta < 2 segundos'"
                );
            }
        }
    }

    @Override
    public String getRuleName() {
        return "Completeness Checker";
    }

    @Override
    public String getRuleDescription() {
        return "Verifica se o requisito possui todos os atributos necessários conforme IREB";
    }
}
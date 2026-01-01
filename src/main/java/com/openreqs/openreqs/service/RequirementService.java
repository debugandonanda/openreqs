package com.openreqs.openreqs.service;

import com.openreqs.openreqs.domain.entity.Requirement;
import com.openreqs.openreqs.domain.entity.Stakeholder;
import com.openreqs.openreqs.domain.enums.RequirementStatus;
import com.openreqs.openreqs.dto.RequirementRequest;
import com.openreqs.openreqs.dto.RequirementResponse;
import com.openreqs.openreqs.dto.StakeholderResponse;
import com.openreqs.openreqs.exception.RequirementViolationException;
import com.openreqs.openreqs.repository.RequirementRepository;
import com.openreqs.openreqs.repository.StakeholderRepository;
import com.openreqs.openreqs.service.quality.AmbiguityChecker;
import com.openreqs.openreqs.service.quality.CompletenessChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final StakeholderRepository stakeholderRepository;
    private final AmbiguityChecker ambiguityChecker;
    private final CompletenessChecker completenessChecker;

    @Transactional
    public RequirementResponse createRequirement(RequirementRequest request) {
        // Validações IREB
        ambiguityChecker.validate(request);
        completenessChecker.validate(request);

        // Verifica stakeholder
        Stakeholder source = stakeholderRepository.findById(request.getSourceId())
                .orElseThrow(() -> new RuntimeException("Stakeholder não encontrado"));

        // Cria requisito
        Requirement requirement = Requirement.builder()
                .requirementId(request.getRequirementId())
                .description(request.getDescription())
                .rationale(request.getRationale())
                .fitCriterion(request.getFitCriterion())
                .type(request.getType())
                .status(RequirementStatus.DRAFT)
                .priority(request.getPriority())
                .source(source)
                .build();

        Requirement saved = requirementRepository.save(requirement);
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RequirementResponse> getAllRequirements() {
        return requirementRepository.findByIsArchivedFalse().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RequirementResponse getRequirementById(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));
        return convertToResponse(requirement);
    }

    @Transactional(readOnly = true)
    public RequirementResponse getRequirementByReqId(String requirementId) {
        Requirement requirement = requirementRepository.findByRequirementId(requirementId)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado: " + requirementId));
        return convertToResponse(requirement);
    }

    @Transactional
    public RequirementResponse updateRequirement(Long id, RequirementRequest request) {
        Requirement existing = requirementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        // IREB: Se estiver APROVADO, cria nova versão
        if (existing.getStatus() == RequirementStatus.APPROVED) {
            existing.archive();
            requirementRepository.save(existing);

            // Cria nova versão
            Requirement newVersion = existing.createNewVersion();
            newVersion.setDescription(request.getDescription());
            newVersion.setRationale(request.getRationale());
            newVersion.setFitCriterion(request.getFitCriterion());
            newVersion.setType(request.getType());
            newVersion.setPriority(request.getPriority());

            // Valida nova versão
            ambiguityChecker.validate(request);
            completenessChecker.validate(request);

            Requirement saved = requirementRepository.save(newVersion);
            return convertToResponse(saved);
        }

        // Validações para atualização normal
        ambiguityChecker.validate(request);
        completenessChecker.validate(request);

        // Atualiza campos
        existing.setDescription(request.getDescription());
        existing.setRationale(request.getRationale());
        existing.setFitCriterion(request.getFitCriterion());
        existing.setType(request.getType());
        existing.setPriority(request.getPriority());

        Requirement updated = requirementRepository.save(existing);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteRequirement(Long id) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        // IREB: Em vez de deletar, arquiva
        requirement.archive();
        requirementRepository.save(requirement);
    }

    @Transactional
    public RequirementResponse changeStatus(Long id, RequirementStatus newStatus) {
        Requirement requirement = requirementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        if (!requirement.getStatus().canChangeTo(newStatus)) {
            throw new RequirementViolationException(
                    "Transição de status inválida",
                    "STATUS_TRANSITION",
                    requirement.getStatus() + " -> " + newStatus,
                    "Status atual: " + requirement.getStatus() +
                            ". Status permitidos: " + getValidTransitions(requirement.getStatus())
            );
        }

        requirement.setStatus(newStatus);
        Requirement updated = requirementRepository.save(requirement);
        return convertToResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<RequirementResponse> getRequirementsByStatus(RequirementStatus status) {
        return requirementRepository.findByStatus(status).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RequirementResponse> searchRequirements(String keyword) {
        return requirementRepository.searchByKeyword(keyword).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private String getValidTransitions(RequirementStatus currentStatus) {
        return switch (currentStatus) {
            case DRAFT -> "REVIEW, REJECTED";
            case REVIEW -> "APPROVED, DRAFT";
            case APPROVED -> "IMPLEMENTED, DEPRECATED";
            case IMPLEMENTED -> "TESTED";
            case TESTED -> "DEPRECATED";
            default -> "Nenhuma transição permitida";
        };
    }

    private RequirementResponse convertToResponse(Requirement requirement) {
        StakeholderResponse stakeholderResponse = null;
        if (requirement.getSource() != null) {
            stakeholderResponse = StakeholderResponse.builder()
                    .id(requirement.getSource().getId())
                    .name(requirement.getSource().getName())
                    .role(requirement.getSource().getRole())
                    .email(requirement.getSource().getEmail())
                    .build();
        }

        List<RequirementResponse> dependencies = requirement.getDependencies().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return RequirementResponse.builder()
                .id(requirement.getId())
                .requirementId(requirement.getRequirementId())
                .description(requirement.getDescription())
                .rationale(requirement.getRationale())
                .fitCriterion(requirement.getFitCriterion())
                .type(requirement.getType())
                .status(requirement.getStatus())
                .version(requirement.getVersion())
                .priority(requirement.getPriority())
                .source(stakeholderResponse)
                .createdAt(requirement.getCreatedAt())
                .lastUpdated(requirement.getLastUpdated())
                .dependencies(dependencies)
                .isArchived(requirement.getIsArchived())
                .build();
    }
}
package com.openreqs.openreqs.service;

import com.openreqs.openreqs.domain.entity.Stakeholder;
import com.openreqs.openreqs.dto.StakeholderRequest;
import com.openreqs.openreqs.dto.StakeholderResponse;
import com.openreqs.openreqs.repository.StakeholderRepository;
import com.openreqs.openreqs.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StakeholderService {

    private final StakeholderRepository stakeholderRepository;
    private final RequirementRepository requirementRepository;

    @Transactional
    public StakeholderResponse createStakeholder(StakeholderRequest request) {
        // Verifica se email já existe
        if (stakeholderRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Stakeholder com este email já existe");
        }

        Stakeholder stakeholder = Stakeholder.builder()
                .name(request.getName())
                .role(request.getRole())
                .email(request.getEmail())
                .responsibilities(request.getResponsibilities())
                .influenceLevel(request.getInfluenceLevel())
                .build();

        Stakeholder saved = stakeholderRepository.save(stakeholder);
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<StakeholderResponse> getAllStakeholders() {
        return stakeholderRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StakeholderResponse getStakeholderById(Long id) {
        Stakeholder stakeholder = stakeholderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder não encontrado"));
        return convertToResponse(stakeholder);
    }

    @Transactional
    public StakeholderResponse updateStakeholder(Long id, StakeholderRequest request) {
        Stakeholder stakeholder = stakeholderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder não encontrado"));

        // Verifica se novo email já existe (para outro stakeholder)
        stakeholderRepository.findByEmail(request.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new RuntimeException("Email já está em uso por outro stakeholder");
                    }
                });

        stakeholder.setName(request.getName());
        stakeholder.setRole(request.getRole());
        stakeholder.setEmail(request.getEmail());
        stakeholder.setResponsibilities(request.getResponsibilities());
        stakeholder.setInfluenceLevel(request.getInfluenceLevel());

        Stakeholder updated = stakeholderRepository.save(stakeholder);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteStakeholder(Long id) {
        Stakeholder stakeholder = stakeholderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stakeholder não encontrado"));

        // Verifica se tem requisitos associados
        long requirementCount = requirementRepository.findBySourceId(id).size();
        if (requirementCount > 0) {
            throw new RuntimeException(
                    String.format("Não é possível deletar. Stakeholder tem %d requisito(s) associado(s).", requirementCount)
            );
        }

        stakeholderRepository.delete(stakeholder);
    }

    private StakeholderResponse convertToResponse(Stakeholder stakeholder) {
        int requirementCount = stakeholder.getRequirements() != null ?
                stakeholder.getRequirements().size() : 0;

        return StakeholderResponse.builder()
                .id(stakeholder.getId())
                .name(stakeholder.getName())
                .role(stakeholder.getRole())
                .email(stakeholder.getEmail())
                .responsibilities(stakeholder.getResponsibilities())
                .influenceLevel(stakeholder.getInfluenceLevel())
                .requirementCount(requirementCount)
                .build();
    }
}
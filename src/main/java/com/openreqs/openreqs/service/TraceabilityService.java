package com.openreqs.openreqs.service;

import com.openreqs.openreqs.domain.entity.Requirement;
import com.openreqs.openreqs.repository.RequirementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TraceabilityService {

    private final RequirementRepository requirementRepository;

    @Transactional
    public void addDependency(Long requirementId, Long dependsOnId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado: " + requirementId));

        Requirement dependency = requirementRepository.findById(dependsOnId)
                .orElseThrow(() -> new RuntimeException("Dependência não encontrada: " + dependsOnId));

        // Verifica dependência circular
        if (createsCircularDependency(requirement, dependency)) {
            throw new RuntimeException("Dependência circular detectada!");
        }

        requirement.getDependencies().add(dependency);
        requirementRepository.save(requirement);
    }

    @Transactional
    public void removeDependency(Long requirementId, Long dependsOnId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        Requirement dependency = requirementRepository.findById(dependsOnId)
                .orElseThrow(() -> new RuntimeException("Dependência não encontrada"));

        requirement.getDependencies().remove(dependency);
        requirementRepository.save(requirement);
    }

    @Transactional(readOnly = true)
    public List<String> getRequirementTraceability(String requirementId) {
        Requirement requirement = requirementRepository.findByRequirementId(requirementId)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        return requirement.getDependencies().stream()
                .map(dep -> String.format("%s (v%s) -> %s",
                        requirementId,
                        requirement.getVersion(),
                        dep.getRequirementId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getImpactAnalysis(String requirementId) {
        Requirement requirement = requirementRepository.findByRequirementId(requirementId)
                .orElseThrow(() -> new RuntimeException("Requisito não encontrado"));

        // Encontra todos os requisitos que dependem deste
        List<Requirement> allRequirements = requirementRepository.findAll();

        return allRequirements.stream()
                .filter(req -> req.getDependencies().contains(requirement))
                .map(req -> String.format("%s depende de %s",
                        req.getRequirementId(),
                        requirementId))
                .collect(Collectors.toList());
    }

    private boolean createsCircularDependency(Requirement requirement, Requirement newDependency) {
        if (requirement.equals(newDependency)) {
            return true;
        }

        // Verifica se o novo dependente já depende do requisito atual
        return checkDependencyChain(newDependency, requirement);
    }

    private boolean checkDependencyChain(Requirement start, Requirement target) {
        if (start.getDependencies().contains(target)) {
            return true;
        }

        for (Requirement dep : start.getDependencies()) {
            if (checkDependencyChain(dep, target)) {
                return true;
            }
        }

        return false;
    }
}
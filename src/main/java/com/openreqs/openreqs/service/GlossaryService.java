package com.openreqs.openreqs.service;

import com.openreqs.openreqs.domain.entity.ProjectGlossary;
import com.openreqs.openreqs.dto.GlossaryRequest;
import com.openreqs.openreqs.dto.GlossaryResponse;
import com.openreqs.openreqs.repository.ProjectGlossaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GlossaryService {

    private final ProjectGlossaryRepository glossaryRepository;

    @Transactional
    public GlossaryResponse createTerm(GlossaryRequest request) {
        // Verifica se termo já existe
        if (glossaryRepository.findByTerm(request.getTerm()).isPresent()) {
            throw new RuntimeException("Termo já existe no glossário");
        }

        ProjectGlossary term = ProjectGlossary.builder()
                .term(request.getTerm())
                .definition(request.getDefinition())
                .acronym(request.getAcronym())
                .relatedTerms(request.getRelatedTerms())
                .businessDomain(request.getBusinessDomain())
                .build();

        ProjectGlossary saved = glossaryRepository.save(term);
        return convertToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<GlossaryResponse> getAllTerms() {
        return glossaryRepository.findAll().stream()
                .map(this::convertToResponse)
                .sorted((t1, t2) -> t1.getTerm().compareToIgnoreCase(t2.getTerm()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public GlossaryResponse getTermById(Long id) {
        ProjectGlossary term = glossaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Termo não encontrado"));
        return convertToResponse(term);
    }

    @Transactional(readOnly = true)
    public GlossaryResponse getTermByName(String termName) {
        ProjectGlossary term = glossaryRepository.findByTerm(termName)
                .orElseThrow(() -> new RuntimeException("Termo não encontrado: " + termName));
        return convertToResponse(term);
    }

    @Transactional
    public GlossaryResponse updateTerm(Long id, GlossaryRequest request) {
        ProjectGlossary term = glossaryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Termo não encontrado"));

        term.setTerm(request.getTerm());
        term.setDefinition(request.getDefinition());
        term.setAcronym(request.getAcronym());
        term.setRelatedTerms(request.getRelatedTerms());
        term.setBusinessDomain(request.getBusinessDomain());

        ProjectGlossary updated = glossaryRepository.save(term);
        return convertToResponse(updated);
    }

    @Transactional
    public void deleteTerm(Long id) {
        if (!glossaryRepository.existsById(id)) {
            throw new RuntimeException("Termo não encontrado");
        }
        glossaryRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<GlossaryResponse> searchTerms(String keyword) {
        return glossaryRepository.findAll().stream()
                .filter(term -> term.getTerm().toLowerCase().contains(keyword.toLowerCase()) ||
                        term.getDefinition().toLowerCase().contains(keyword.toLowerCase()))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GlossaryResponse convertToResponse(ProjectGlossary term) {
        return GlossaryResponse.builder()
                .id(term.getId())
                .term(term.getTerm())
                .definition(term.getDefinition())
                .acronym(term.getAcronym())
                .relatedTerms(term.getRelatedTerms())
                .businessDomain(term.getBusinessDomain())
                .createdAt(term.getCreatedAt() != null ?
                        term.getCreatedAt().toString() : "")
                .lastUpdated(term.getLastUpdated() != null ?
                        term.getLastUpdated().toString() : "")
                .build();
    }
}
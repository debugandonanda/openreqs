package com.openreqs.openreqs.controller;

import com.openreqs.openreqs.domain.enums.RequirementStatus;
import com.openreqs.openreqs.dto.RequirementRequest;
import com.openreqs.openreqs.dto.RequirementResponse;
import com.openreqs.openreqs.service.RequirementService;
import com.openreqs.openreqs.service.TraceabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requirements")
@RequiredArgsConstructor
@Tag(name = "Requirements", description = "API para gerenciamento de requisitos conforme IREB")
public class RequirementController {

    private final RequirementService requirementService;
    private final TraceabilityService traceabilityService;

    @PostMapping
    @Operation(summary = "Criar novo requisito", description = "Cria um requisito com validações IREB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Requisito criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Violação das regras IREB"),
            @ApiResponse(responseCode = "404", description = "Stakeholder não encontrado")
    })
    public ResponseEntity<RequirementResponse> createRequirement(
            @Valid @RequestBody RequirementRequest request) {
        RequirementResponse response = requirementService.createRequirement(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os requisitos", description = "Retorna todos os requisitos não arquivados")
    public ResponseEntity<List<RequirementResponse>> getAllRequirements() {
        List<RequirementResponse> requirements = requirementService.getAllRequirements();
        return ResponseEntity.ok(requirements);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar requisito por ID")
    public ResponseEntity<RequirementResponse> getRequirementById(
            @Parameter(description = "ID do requisito") @PathVariable Long id) {
        RequirementResponse response = requirementService.getRequirementById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/req-id/{requirementId}")
    @Operation(summary = "Buscar requisito por ID único")
    public ResponseEntity<RequirementResponse> getRequirementByReqId(
            @Parameter(description = "ID único do requisito (ex: REQ-001)")
            @PathVariable String requirementId) {
        RequirementResponse response = requirementService.getRequirementByReqId(requirementId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar requisito", description = "Atualiza um requisito. Se estiver APROVADO, cria nova versão")
    public ResponseEntity<RequirementResponse> updateRequirement(
            @PathVariable Long id,
            @Valid @RequestBody RequirementRequest request) {
        RequirementResponse response = requirementService.updateRequirement(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Arquivar requisito", description = "Arquiva o requisito em vez de deletar")
    public ResponseEntity<Void> deleteRequirement(@PathVariable Long id) {
        requirementService.deleteRequirement(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do requisito", description = "Muda o status seguindo a máquina de estados IREB")
    public ResponseEntity<RequirementResponse> changeStatus(
            @PathVariable Long id,
            @RequestParam RequirementStatus status) {
        RequirementResponse response = requirementService.changeStatus(id, status);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Buscar requisitos por status")
    public ResponseEntity<List<RequirementResponse>> getRequirementsByStatus(
            @PathVariable RequirementStatus status) {
        List<RequirementResponse> responses = requirementService.getRequirementsByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar requisitos por palavra-chave")
    public ResponseEntity<List<RequirementResponse>> searchRequirements(
            @RequestParam String keyword) {
        List<RequirementResponse> responses = requirementService.searchRequirements(keyword);
        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{requirementId}/dependencies/{dependsOnId}")
    @Operation(summary = "Adicionar dependência entre requisitos")
    public ResponseEntity<Void> addDependency(
            @PathVariable Long requirementId,
            @PathVariable Long dependsOnId) {
        traceabilityService.addDependency(requirementId, dependsOnId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{requirementId}/traceability")
    @Operation(summary = "Obter rastreabilidade do requisito")
    public ResponseEntity<List<String>> getTraceability(
            @PathVariable String requirementId) {
        List<String> traceability = traceabilityService.getRequirementTraceability(requirementId);
        return ResponseEntity.ok(traceability);
    }

    @GetMapping("/{requirementId}/impact")
    @Operation(summary = "Análise de impacto do requisito")
    public ResponseEntity<List<String>> getImpactAnalysis(
            @PathVariable String requirementId) {
        List<String> impact = traceabilityService.getImpactAnalysis(requirementId);
        return ResponseEntity.ok(impact);
    }
}
package com.openreqs.openreqs.controller;

import com.openreqs.openreqs.dto.StakeholderRequest;
import com.openreqs.openreqs.dto.StakeholderResponse;
import com.openreqs.openreqs.service.StakeholderService;
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
@RequestMapping("/api/stakeholders")
@RequiredArgsConstructor
@Tag(name = "Stakeholders", description = "API para gerenciamento de stakeholders conforme IREB")
public class StakeholderController {

    private final StakeholderService stakeholderService;

    @PostMapping
    @Operation(summary = "Criar novo stakeholder", description = "Registra um novo stakeholder no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Stakeholder criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "Stakeholder com este email já existe")
    })
    public ResponseEntity<StakeholderResponse> createStakeholder(
            @Valid @RequestBody StakeholderRequest request) {
        StakeholderResponse response = stakeholderService.createStakeholder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os stakeholders", description = "Retorna todos os stakeholders cadastrados")
    public ResponseEntity<List<StakeholderResponse>> getAllStakeholders() {
        List<StakeholderResponse> responses = stakeholderService.getAllStakeholders();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar stakeholder por ID", description = "Retorna um stakeholder específico pelo ID")
    public ResponseEntity<StakeholderResponse> getStakeholderById(
            @Parameter(description = "ID do stakeholder") @PathVariable Long id) {
        StakeholderResponse response = stakeholderService.getStakeholderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Buscar stakeholder por email", description = "Retorna um stakeholder pelo email")
    public ResponseEntity<StakeholderResponse> getStakeholderByEmail(
            @Parameter(description = "Email do stakeholder") @PathVariable String email) {
        // Note: Você precisará adicionar este método no StakeholderService
        // Por enquanto, vamos buscar por ID
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar stakeholder", description = "Atualiza os dados de um stakeholder existente")
    public ResponseEntity<StakeholderResponse> updateStakeholder(
            @PathVariable Long id,
            @Valid @RequestBody StakeholderRequest request) {
        StakeholderResponse response = stakeholderService.updateStakeholder(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover stakeholder", description = "Remove um stakeholder do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Stakeholder removido com sucesso"),
            @ApiResponse(responseCode = "404", description = "Stakeholder não encontrado"),
            @ApiResponse(responseCode = "409", description = "Stakeholder tem requisitos associados")
    })
    public ResponseEntity<Void> deleteStakeholder(@PathVariable Long id) {
        stakeholderService.deleteStakeholder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/requirements")
    @Operation(summary = "Listar requisitos do stakeholder", description = "Retorna todos os requisitos associados a um stakeholder")
    public ResponseEntity<List<Object>> getStakeholderRequirements(@PathVariable Long id) {
        // Este método retornaria os requisitos do stakeholder
        // Você precisaria implementar no service
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
package com.accenture.modules.candidat.web;

import com.accenture.modules.candidat.service.usecase.CandidatServiceUseCase;
import com.accenture.modules.candidat.service.domain.Candidat;
import com.accenture.modules.candidat.web.dto.CandidatRequestDto;
import com.accenture.modules.candidat.web.dto.CandidatResponseDto;
import com.accenture.modules.candidat.web.mapper.CandidatDtoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidats")
public class CandidateController {

    private final CandidatServiceUseCase candidateService;
    private final CandidatDtoMapper candidatDtoMapper;

    public CandidateController(CandidatServiceUseCase candidateService, CandidatDtoMapper candidatDtoMapper) {
        this.candidateService = candidateService;
        this.candidatDtoMapper = candidatDtoMapper;
    }

    @PostMapping
    public ResponseEntity<CandidatResponseDto> create(@RequestBody CandidatRequestDto request) {
        Candidat created = candidateService.createCandidate(request.firstName(), request.lastName(), request.email());
        return ResponseEntity.created(URI.create("/api/candidat/candidates/" + created.getId())).body(candidatDtoMapper.toCandidatResponseDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidatResponseDto> getById(@PathVariable("id") UUID id) {
        return candidateService.getCandidateById(id)
                .map(candidat -> ResponseEntity.ok(candidatDtoMapper.toCandidatResponseDto(candidat)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<CandidatResponseDto> list() {
        return candidateService.getAllCandidates()
                .stream()
                .map(candidatDtoMapper::toCandidatResponseDto)
                .toList();
    }

}

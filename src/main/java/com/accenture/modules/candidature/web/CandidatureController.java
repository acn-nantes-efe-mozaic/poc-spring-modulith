package com.accenture.modules.candidature.web;

import com.accenture.modules.candidature.service.CandidatureService;
import com.accenture.modules.candidature.service.domain.Candidature;
import com.accenture.modules.candidature.service.usecase.CandidatureServiceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/candidature")
public class CandidatureController {

    private final CandidatureServiceUseCase candidatureService;

    public CandidatureController(CandidatureServiceUseCase candidatureService) {
        this.candidatureService = candidatureService;
    }

    // Create a candidature linked to a candidateId and offerId
    @PostMapping("/candidatures")
    public ResponseEntity<?> create(@RequestBody CreateCandidatureRequest request) {
        try {
            Candidature created = candidatureService.createCandidature(request.candidateId(), request.offerId());
            return ResponseEntity
                    .created(URI.create("/api/candidature/candidatures/" + created.getId()))
                    .body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get candidature by id
    @GetMapping("/candidatures/{id}")
    public ResponseEntity<Candidature> getById(@PathVariable("id") UUID id) {
        return candidatureService.getCandidatureById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // List all candidatures for a specific candidate
    @GetMapping("/candidates/{candidateId}/candidatures")
    public List<Candidature> listForCandidate(@PathVariable("candidateId") UUID candidateId) {
        return candidatureService.getCandidaturesByCandidate(candidateId);
    }

    public record CreateCandidatureRequest(UUID candidateId, UUID offerId) { }
}

package com.accenture.modules.candidature.service.usecase;

import com.accenture.modules.candidature.service.domain.Candidature;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidatureServiceUseCase {
    Candidature createCandidature(UUID candidateId, UUID offerId);

    Optional<Candidature> getCandidatureById(UUID id);

    List<Candidature> getCandidaturesByCandidate(UUID candidateId);
}

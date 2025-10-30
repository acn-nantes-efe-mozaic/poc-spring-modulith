package com.accenture.modules.candidat.service.usecase;

import com.accenture.modules.candidat.service.domain.Candidat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidatServiceUseCase {
    Candidat createCandidate(String firstName, String lastName, String email);

    Optional<Candidat> getCandidateById(UUID id);

    List<Candidat> getAllCandidates();
}

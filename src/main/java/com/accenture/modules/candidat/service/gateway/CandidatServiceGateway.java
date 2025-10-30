package com.accenture.modules.candidat.service.gateway;

import com.accenture.modules.candidat.service.domain.Candidat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CandidatServiceGateway {
    boolean isCandidatExiste(UUID candidateId);
    
    /**
     * Récupère l'email d'un candidat
     * @param candidateId L'identifiant du candidat
     * @return L'email du candidat
     * @throws IllegalArgumentException si le candidat n'existe pas
     */
    String getEmailCandidat(UUID candidateId);
}

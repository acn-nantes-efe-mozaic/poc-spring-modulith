package com.accenture.modules.candidat.service;

import com.accenture.modules.candidat.repository.CandidatRepository;
import com.accenture.modules.candidat.repository.entity.CandidatEntity;
import com.accenture.modules.candidat.repository.mapper.CandidatEntityMapper;
import com.accenture.modules.candidat.service.domain.Candidat;
import com.accenture.modules.candidat.service.gateway.CandidatServiceGateway;
import com.accenture.modules.candidat.service.usecase.CandidatServiceUseCase;
import com.accenture.modules.candidature.repository.mapper.CandidatureEntityMapper;
import com.accenture.modules.shared.events.CandidatCreeEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Application service for Candidate operations: create and consult candidates.
 */
@Service
public class CandidatService implements CandidatServiceUseCase, CandidatServiceGateway {

    private final CandidatRepository candidatRepository;
    private final CandidatEntityMapper candidatEntityMapper;
    private final ApplicationEventPublisher eventPublisher;

    public CandidatService(
        CandidatRepository candidatRepository, 
        CandidatEntityMapper candidatEntityMapper,
        ApplicationEventPublisher eventPublisher
    ) {
        this.candidatRepository = candidatRepository;
        this.candidatEntityMapper = candidatEntityMapper;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Creates and stores a new Candidate.
     */
    @Override
    @Transactional
    public Candidat createCandidate(String firstName, String lastName, String email) {
        Candidat candidate = Candidat.of(firstName, lastName, email);
        CandidatEntity ce = candidatEntityMapper.toCandidatEntity(candidate);
        Candidat savedCandidate = candidatEntityMapper.toCandidat(candidatRepository.save(ce));
        
        // Publier l'événement de création de candidat
        eventPublisher.publishEvent(
            new CandidatCreeEvent(
                savedCandidate.getId(),
                savedCandidate.getEmail(),
                savedCandidate.getFirstName(),
                savedCandidate.getLastName()
            )
        );
        
        return savedCandidate;
    }

    /**
     * Retrieves a candidate by its id.
     */
    @Override
    public Optional<Candidat> getCandidateById(UUID id) {
        return candidatRepository.findById(id)
                .map(candidatEntityMapper::toCandidat);
    }

    /**
     * Returns all candidates.
     */
    @Override
    public List<Candidat> getAllCandidates() {
        return candidatRepository.findAll().stream()
                .map(candidatEntityMapper::toCandidat)
                .toList();
    }

    @Override
    public boolean isCandidatExiste(UUID candidateId) {
        return candidateId != null && candidatRepository.existsById(candidateId);
    }
    
    @Override
    public String getEmailCandidat(UUID candidateId) {
        return candidatRepository.findById(candidateId)
                .map(CandidatEntity::getEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidat non trouvé: " + candidateId));
    }
}

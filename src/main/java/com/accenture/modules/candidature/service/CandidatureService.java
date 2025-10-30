package com.accenture.modules.candidature.service;

import com.accenture.modules.candidat.service.gateway.CandidatServiceGateway;
import com.accenture.modules.candidature.repository.CandidatureEntity;
import com.accenture.modules.candidature.repository.CandidatureRepository;
import com.accenture.modules.candidature.repository.mapper.CandidatureEntityMapper;
import com.accenture.modules.candidature.service.domain.Candidature;
import com.accenture.modules.shared.events.CandidatureCreatedEvent;
import com.accenture.modules.candidature.service.usecase.CandidatureServiceUseCase;
import com.accenture.modules.recruteur.service.gateway.OfferServiceGateway;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service d'application pour la gestion des candidatures.
 *
 * Intégration avec les autres modules (compatible Spring Modulith) :
 * - Ce service dépend de deux ports d'application (gateways) exposés par d'autres modules :
 *   - com.accenture.modules.candidat.service.gateway.CandidatServiceGateway
 *   - com.accenture.modules.recruteur.service.gateway.OfferServiceGateway
 * - Ces packages « gateway » sont annotés avec @org.springframework.modulith.NamedInterface dans leur package-info.java,
 *   ce qui autorise les autres modules à dépendre de ces interfaces sans rompre les frontières entre modules.
 * - Spring injecte les implémentations concrètes (CandidatService, OfferService) car ce sont des beans @Service
 *   qui implémentent ces interfaces de port. CandidatureService ne connaît que les interfaces, pas les implémentations.
 *
 * Déroulement à l'exécution dans createCandidature(...):
 * 1. Appeler candidateService.isCandidatExiste(candidateId) pour vérifier que le candidat existe.
 * 2. Appeler offerService.isOffreExiste(offerId) pour vérifier que l'offre existe.
 * 3. Si les deux existent, persister une CandidatureEntity et la mapper vers le modèle de domaine.
 *
 * Ce fonctionnement garantit le découplage entre modules tout en permettant des vérifications synchrones inter‑modules.
 */
@Service
public class CandidatureService implements CandidatureServiceUseCase {

    private final CandidatureRepository candidatureRepository;
    private final CandidatureEntityMapper candidatureEntityMapper;
    private final CandidatServiceGateway candidateService;
    private final OfferServiceGateway offerService;
    private final ApplicationEventPublisher eventPublisher;

    public CandidatureService(CandidatureRepository candidatureRepository,
                              CandidatureEntityMapper candidatureEntityMapper,
                              CandidatServiceGateway candidateService,
                              OfferServiceGateway offerService,
                              ApplicationEventPublisher eventPublisher) {
        this.candidatureRepository = candidatureRepository;
        this.candidatureEntityMapper = candidatureEntityMapper;
        this.candidateService = candidateService;
        this.offerService = offerService;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Crée une candidature pour un candidat et une offre existants.
     * Lance IllegalArgumentException si le candidat ou l'offre n'existent pas.
     */
    @Transactional
    @Override
    public Candidature createCandidature(UUID candidateId, UUID offerId) {
        if (candidateId == null || offerId == null) {
            throw new IllegalArgumentException("candidateId and offerId must be provided");
        }

        // Vérifier l'existence du candidat
        if (!candidateService.isCandidatExiste(candidateId))
                throw new IllegalArgumentException("Candidate not found: " + candidateId);

        // Vérifier l'existence de l'offre
        if (!offerService.isOffreExiste(offerId))
            throw  new IllegalArgumentException("Offer not found: " + offerId);

        // Créer et persister la candidature
        CandidatureEntity entity = new CandidatureEntity(candidateId, offerId, Instant.now());
        CandidatureEntity saved = candidatureRepository.save(entity);

        // Publier un événement de création pour que le module Recruteur puisse réagir (incrémenter nbCandidatures)
        eventPublisher.publishEvent(new CandidatureCreatedEvent(
                saved.getId(), saved.getCandidateId(), saved.getOfferId(), saved.getCreatedAt()
        ));

        return candidatureEntityMapper.toCandidature(saved);

    }

    /**
     * Récupère une candidature par identifiant.
     */
    @Override
    public Optional<Candidature> getCandidatureById(UUID id) {
        return candidatureRepository
                .findById(id).map(candidatureEntityMapper::toCandidature);
    }

    /**
     * Liste toutes les candidatures pour l'identifiant de candidat donné.
     */
    @Override
    public List<Candidature> getCandidaturesByCandidate(UUID candidateId) {
        return candidatureRepository
                .findByCandidateId(candidateId)
                .stream()
                .map(candidatureEntityMapper::toCandidature)
                .toList();
    }
}

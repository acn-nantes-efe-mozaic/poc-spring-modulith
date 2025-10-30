package com.accenture.modules.recruteur.service;

import com.accenture.modules.recruteur.service.domain.Offer;
import com.accenture.modules.recruteur.repository.OfferRepository;
import com.accenture.modules.recruteur.repository.entity.OfferEntity;
import com.accenture.modules.recruteur.repository.mapper.OfferEntityMapper;
import com.accenture.modules.recruteur.service.gateway.OfferServiceGateway;
import com.accenture.modules.recruteur.service.usecase.OfferServiceUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Application service for Recruiters to create and consult job offers.
 */
@Service
public class OfferService implements OfferServiceUseCase, OfferServiceGateway {

    private static final Logger log = LoggerFactory.getLogger(OfferService.class);

    private final OfferRepository offerRepository;
    private final OfferEntityMapper offerEntityMapper;

    public OfferService(OfferRepository offerRepository, OfferEntityMapper offerEntityMapper) {
        this.offerRepository = offerRepository;
        this.offerEntityMapper = offerEntityMapper;
    }

    /**
     * Creates a new Offer and stores it in database.
     */
    @Override
    public Offer createOffer(String title, String description) {
        Offer offer = Offer.of(title, description);
        log.info("Creating new offer: {}", offer);
        OfferEntity entity = offerEntityMapper.toOfferEntity(offer);
        return offerEntityMapper.toOffer(offerRepository.save(entity));
    }

    /**
     * Retrieves an offer by its id.
     */
    @Override
    public Optional<Offer> getOfferById(UUID id) {
        return offerRepository.findById(id).map(offerEntityMapper::toOffer);
    }

    /**
     * Returns all offers.
     */
    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll().stream().map(offerEntityMapper::toOffer).toList();
    }

    @Override
    public boolean isOffreExiste(UUID offreId) {
        return offreId != null && offerRepository.existsById(offreId);
    }
    
    @Override
    public String getNomOffre(UUID offerId) {
        return offerRepository.findById(offerId)
                .map(OfferEntity::getTitle)
                .orElseThrow(() -> new IllegalArgumentException("Offre non trouvée: " + offerId));
    }
}
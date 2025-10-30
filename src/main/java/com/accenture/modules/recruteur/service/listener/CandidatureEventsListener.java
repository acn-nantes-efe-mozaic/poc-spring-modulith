package com.accenture.modules.recruteur.service.listener;

import com.accenture.modules.shared.events.CandidatureCreatedEvent;
import com.accenture.modules.recruteur.repository.OfferRepository;
import com.accenture.modules.recruteur.repository.entity.OfferEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Listens to candidature events and updates recruiter domain accordingly.
 */
@Component("CandidatureEventsListenerForRecruteur")
public class CandidatureEventsListener {

    private static final Logger log = LoggerFactory.getLogger(CandidatureEventsListener.class);

    private final OfferRepository offerRepository;

    public CandidatureEventsListener(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    /**
     * When a candidature is created, increment the number of candidatures on the corresponding offer.
     */
    @ApplicationModuleListener
    void onCandidatureCreated(CandidatureCreatedEvent event) {
        log.info("Handling CandidatureCreatedEvent for offer {}", event.offerId());
        Optional<OfferEntity> optionalOffer = offerRepository.findById(event.offerId());
        if (optionalOffer.isEmpty()) {
            log.warn("Offer {} not found while handling CandidatureCreatedEvent; skipping.", event.offerId());
            return;
        }
        OfferEntity offer = optionalOffer.get();
        offer.setNbCandidatures(offer.getNbCandidatures() + 1);
        offerRepository.save(offer);
        log.info("Incremented nbCandidatures for offer {} to {}", offer.getId(), offer.getNbCandidatures());
    }
}

package com.accenture.modules.candidat.service.listener;

import com.accenture.modules.candidat.repository.CandidatRepository;
import com.accenture.modules.candidat.repository.entity.CandidatEntity;
import com.accenture.modules.shared.events.CandidatureCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Listens to candidature events and updates recruiter domain accordingly.
 */
@Component("CandidatureEventsListenerForCandidat")
public class CandidatureEventsListener {

    private static final Logger log = LoggerFactory.getLogger(CandidatureEventsListener.class);

    private final CandidatRepository candidatRepository;

    public CandidatureEventsListener(CandidatRepository candidatRepository) {
        this.candidatRepository = candidatRepository;
    }

    /**
     * When a candidature is created, increment the number of candidatures on the corresponding offer.
     */
    @ApplicationModuleListener
    void onCandidatureCreated(CandidatureCreatedEvent event) {
        Optional<CandidatEntity> optionalCandidat = candidatRepository.findById(event.candidateId());
        if (optionalCandidat.isEmpty()) {
            log.warn("Offer {} not found while handling CandidatureCreatedEvent; skipping.", event.candidateId());
            return;
        }
        CandidatEntity candidat = optionalCandidat.get();
        candidat.setNombreCandidatures(candidat.getNombreCandidatures() + 1);
        candidatRepository.save(candidat);
        log.info("Incremented nbCandidatures for candidat {} to {}", candidat.getId(), candidat.getNombreCandidatures());
    }
}

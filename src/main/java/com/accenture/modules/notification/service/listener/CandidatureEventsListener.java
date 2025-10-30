package com.accenture.modules.notification.service.listener;

import com.accenture.modules.candidat.service.gateway.CandidatServiceGateway;
import com.accenture.modules.notification.service.EmailService;
import com.accenture.modules.recruteur.service.gateway.OfferServiceGateway;
import com.accenture.modules.shared.events.CandidatureCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Écouteur d'événements liés aux candidatures pour envoyer des notifications.
 */
@Slf4j
@Component("CandidatureEventsListenerForNotification")
@RequiredArgsConstructor
public class CandidatureEventsListener {

    private final EmailService emailService;
    private final CandidatServiceGateway candidatService;
    private final OfferServiceGateway offerService;

    @ApplicationModuleListener
    public void onCandidatureCreated(CandidatureCreatedEvent event) {
        // Récupérer les informations du candidat et de l'offre
        String emailCandidat = candidatService.getEmailCandidat(event.candidateId());
        String nomOffre = offerService.getNomOffre(event.offerId());
        
        String subject = "Confirmation de votre candidature";
        String content = String.format("""
            <html>
                <body>
                    <h1>Confirmation de candidature</h1>
                    <p>Votre candidature pour le poste de <strong>%s</strong> a bien été enregistrée.</p>
                    <p>Référence de votre candidature : %s</p>
                    <p>Date de candidature : %s</p>
                    <p>Nous examinerons votre profil avec attention et reviendrons vers vous rapidement.</p>
                    <p>Cordialement,<br>L'équipe Recrutement</p>
                </body>
            </html>
            """, 
            nomOffre,
            event.id().toString().substring(0, 8) + "...",
            event.createdAt().toString()
        );

        try {
            emailService.sendHtmlEmail(emailCandidat, subject, content);
            log.info("Email de confirmation de candidature envoyé à {}", emailCandidat);
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de confirmation à " + emailCandidat, e);
        }
    }
}

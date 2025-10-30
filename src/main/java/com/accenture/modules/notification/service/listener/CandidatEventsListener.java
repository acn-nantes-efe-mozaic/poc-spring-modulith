package com.accenture.modules.notification.service.listener;

import com.accenture.modules.notification.service.EmailService;
import com.accenture.modules.shared.events.CandidatCreeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Component;

/**
 * Écouteur d'événements liés aux candidats pour envoyer des notifications.
 */
@Slf4j
@Component("CandidatEventsListenerForNotification")
@RequiredArgsConstructor
public class CandidatEventsListener {

    private final EmailService emailService;

    @ApplicationModuleListener()
    public void onCandidatCree(CandidatCreeEvent event) {
        String subject = "Bienvenue sur notre plateforme de recrutement";
        String content = String.format("""
            <html>
                <body>
                    <h1>Bienvenue %s %s !</h1>
                    <p>Merci de vous être inscrit sur notre plateforme de recrutement.</p>
                    <p>Votre compte a été créé avec succès avec l'email : %s</p>
                    <p>Cordialement,<br>L'équipe Recrutement</p>
                </body>
            </html>
            """, 
            event.firstName(), 
            event.lastName(),
            event.email()
        );

        try {
            emailService.sendHtmlEmail(event.email(), subject, content);
            log.info("Email de bienvenue envoyé à {}", event.email());
        } catch (Exception e) {
            log.error("Erreur lors de l'envoi de l'email de bienvenue à " + event.email(), e);
        }
    }
}

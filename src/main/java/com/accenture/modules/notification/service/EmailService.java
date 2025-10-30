package com.accenture.modules.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service factice pour l'envoi d'emails.
 * En environnement de développement, les emails sont simplement loggés.
 */
@Service
public class EmailService {
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    /**
     * Envoie un email de manière asynchrone
     * @param to Destinataire
     * @param subject Sujet de l'email
     * @param content Contenu de l'email
     */
    public void sendEmail(String to, String subject, String content) {
        log.info("""
            \n=== EMAIL ENVOYÉ ===
À: {}
Sujet: {}
Contenu:
{}
==================""", to, subject, content);
    }

    /**
     * Envoie un email HTML de manière asynchrone
     * @param to Destinataire
     * @param subject Sujet de l'email
     * @param htmlContent Contenu HTML de l'email
     */
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        log.info("""
            \n=== EMAIL HTML ENVOYÉ ===
À: {}
Sujet: {}
Contenu HTML (version texte):
{}
==========================""", to, subject, htmlContent.replaceAll("<[^>]*>", ""));
    }
}

package com.accenture.modules.notification.service.domain;

import lombok.Builder;

/**
 * DTO pour la demande d'envoi d'email
 */
@Builder
public record EmailRequest(
    String to,
    String subject,
    String content,
    boolean isHtml
) {
    public static EmailRequest textEmail(String to, String subject, String content) {
        return new EmailRequest(to, subject, content, false);
    }

    public static EmailRequest htmlEmail(String to, String subject, String content) {
        return new EmailRequest(to, subject, content, true);
    }
}

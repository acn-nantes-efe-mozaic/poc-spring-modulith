package com.accenture.modules.notification.web;

import com.accenture.modules.notification.service.domain.EmailRequest;
import com.accenture.modules.notification.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emails")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<Void> sendEmail(@RequestBody EmailRequest request) {
        if (request.isHtml()) {
            emailService.sendHtmlEmail(request.to(), request.subject(), request.content());
        } else {
            emailService.sendEmail(request.to(), request.subject(), request.content());
        }
        return ResponseEntity.accepted().build();
    }
}

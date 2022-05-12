package com.jam.controller;

import com.jam.model.Mail;
import com.jam.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * API controller for the email service.
 */
@RestController
@RequestMapping("/api/email")
@Slf4j
public class EmailController {
    @Autowired
    private EmailService emailService;

    /**
     * Sends a mail message to an email.
     * @param mail The contents of the mail.
     */
    @PostMapping
    public ResponseEntity<Void> sendMail(@RequestBody Mail mail) {
        try {
            emailService.sendMail(mail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Sends a mail with file attachments to an email.
     * @param mail The contents of the mail.
     * @param files The file attachments of the mail.
     */
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Void> sendMailWithAttachments(@RequestPart(name = "mail") Mail mail,
                         @RequestPart(name = "attachments", required = false) MultipartFile[] files) {
        mail.setAttachments(files);
        try {
            emailService.sendMail(mail);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("[POST] Failed to send email: " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}

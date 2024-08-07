package com.uba.ejercicio.services.impl;

import com.uba.ejercicio.configuration.MimeMessageHelperFactory;
import com.uba.ejercicio.exceptions.MessagingRuntimeException;
import com.uba.ejercicio.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    private final MimeMessageHelperFactory mimeMessageHelperFactory;

    public EmailServiceImpl(JavaMailSender javaMailSender, MimeMessageHelperFactory mimeMessageHelperFactory) {
        this.javaMailSender = javaMailSender;
        this.mimeMessageHelperFactory = mimeMessageHelperFactory;
    }

    @Value("${mail.from}")
    private String from;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = mimeMessageHelperFactory.create(mimeMessage, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new MessagingRuntimeException("Error sending email", e);
        }
    }
}

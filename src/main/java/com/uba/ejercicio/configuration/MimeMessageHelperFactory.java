package com.uba.ejercicio.configuration;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.MimeMessageHelper;

@Configuration
public class MimeMessageHelperFactory {

    public MimeMessageHelper create(MimeMessage mimeMessage, String encoding) throws MessagingException {
        return new MimeMessageHelper(mimeMessage, encoding);
    }
}
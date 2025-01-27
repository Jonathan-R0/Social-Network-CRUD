package com.uba.ejercicio.services;

import com.uba.ejercicio.configuration.MimeMessageHelperFactory;
import com.uba.ejercicio.services.impl.EmailServiceImpl;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.annotation.DirtiesContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class EmailServiceTest {

    private final EmailService emailService;
    private final JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);
    private final MimeMessageHelperFactory mimeMessageHelperFactory = Mockito.mock(MimeMessageHelperFactory.class);

    public EmailServiceTest() {
        this.emailService = new EmailServiceImpl(javaMailSender, mimeMessageHelperFactory);
    }

    @Test
    public void testSendEmailShouldSendEmail() throws MessagingException {
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = Mockito.mock(MimeMessageHelper.class);

        doNothing().when(javaMailSender).send(any(MimeMessage.class));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mimeMessageHelperFactory.create(any(MimeMessage.class), eq("utf-8")))
                .thenReturn(mimeMessageHelper);

        emailService.sendEmail("to", "subject", "body");
        Mockito.verify(javaMailSender).send(any(MimeMessage.class));
    }
}
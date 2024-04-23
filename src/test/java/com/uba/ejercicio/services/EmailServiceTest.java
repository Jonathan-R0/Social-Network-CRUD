package com.uba.ejercicio.services;

import com.uba.ejercicio.configuration.MimeMessageHelperFactory;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    private final MimeMessageHelperFactory mimeMessageHelperFactory = Mockito.mock(MimeMessageHelperFactory.class);


    @Test
    public void testSendEmailShouldSendEmail() throws MessagingException {
        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
        MimeMessageHelper mimeMessageHelper = Mockito.mock(MimeMessageHelper.class);

        doNothing().when(javaMailSender).send(Mockito.any(MimeMessage.class));
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(mimeMessageHelperFactory.create(Mockito.any(MimeMessage.class), eq("utf-8")))
                .thenReturn(mimeMessageHelper);

        emailService.sendEmail("to", "subject", "body");
        Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
    }

}

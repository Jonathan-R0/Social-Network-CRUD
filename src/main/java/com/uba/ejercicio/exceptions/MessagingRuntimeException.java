package com.uba.ejercicio.exceptions;

import jakarta.mail.MessagingException;

public class MessagingRuntimeException extends RuntimeException {
    public MessagingRuntimeException(String message, MessagingException cause) {
        super(message, cause);
    }
}

package com.uba.ejercicio.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ProfileServiceExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {UserNotFoundException.class})
    protected ResponseEntity handleConflict(UserNotFoundException exception, WebRequest request) {
        return errorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }


    private static ResponseEntity errorResponse(HttpStatus status, String messageToDisplay) {
        // Esto nos va a permitir que nuestra respuesta sea un JSON, mas parecido a lo que veriamos en la mayoria de las APIs que vamos a consumir/construir
        Map<String, String> data = new HashMap<>();
        data.put("error", messageToDisplay);
        return new ResponseEntity<>(data, status);
    }
}
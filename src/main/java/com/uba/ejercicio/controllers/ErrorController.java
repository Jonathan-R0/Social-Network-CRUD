package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.ErrorResponseDto;
import com.uba.ejercicio.exceptions.GenderNotFoundException;
import com.uba.ejercicio.exceptions.UnavailableRoleException;
import com.uba.ejercicio.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ErrorController {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleException(Exception ex) {
        return new ErrorResponseDto("Error: " + ex.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(UnavailableRoleException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleUnavailableRoleException(UnavailableRoleException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponseDto handleConflict(UserNotFoundException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {GenderNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ErrorResponseDto handleConflict(GenderNotFoundException ex) {
        return new ErrorResponseDto(ex);
    }


}

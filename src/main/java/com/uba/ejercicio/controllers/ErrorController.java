package com.uba.ejercicio.controllers;

import com.uba.ejercicio.dto.ErrorResponseDto;
import com.uba.ejercicio.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    public ErrorResponseDto handleConflict(UnavailableRoleException ex) {
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

    @ResponseBody
    @ExceptionHandler(value = {MessagingRuntimeException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ErrorResponseDto handleConflict(MessagingRuntimeException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {TokenException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponseDto handleConflict(TokenException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {BadCredentialsException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ErrorResponseDto handleConflict(BadCredentialsException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {PasswordResetException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponseDto handleConflict(PasswordResetException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {SelfFollowException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ErrorResponseDto handleConflict(SelfFollowException ex) {
        return new ErrorResponseDto(ex);
    }

    @ResponseBody
    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    protected ErrorResponseDto handleConflict(UserAlreadyExistsException ex) {
        return new ErrorResponseDto(ex);
    }

}

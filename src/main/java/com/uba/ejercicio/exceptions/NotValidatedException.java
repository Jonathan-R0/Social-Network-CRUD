package com.uba.ejercicio.exceptions;

public class NotValidatedException extends RuntimeException {

    public NotValidatedException() {
        super("User not validated");
    }

}


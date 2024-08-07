package com.uba.ejercicio.exceptions;

public class UnavailableRoleException extends RuntimeException {

    public UnavailableRoleException(String role) {
        super(
            String.format("Role '%s' is not available", role)
        );
    }

}

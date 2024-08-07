package com.uba.ejercicio.exceptions;

public class SelfFollowException extends RuntimeException {
    public SelfFollowException() {
        super("You can't follow or unfollow yourself");
    }
}

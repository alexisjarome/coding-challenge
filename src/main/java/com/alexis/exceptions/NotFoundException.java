package com.alexis.exceptions;

public class NotFoundException extends RestException {

    public NotFoundException(String message) {
        super(404, message);
    }
}

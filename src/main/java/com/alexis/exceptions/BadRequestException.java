package com.alexis.exceptions;

public class BadRequestException extends RestException {

    public BadRequestException(String message) {
        super(400, message);
    }
}

package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class BootcampException extends RuntimeException {

    private final ExceptionStatusCode statusCode;
    private final int status;

    public BootcampException(ExceptionStatusCode statusCode, String message, int status) {
        super(message);
        this.statusCode = statusCode;
        this.status = status;
    }

}

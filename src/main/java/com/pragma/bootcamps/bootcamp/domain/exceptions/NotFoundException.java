package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class NotFoundException extends BootcampException {
    public NotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }
}

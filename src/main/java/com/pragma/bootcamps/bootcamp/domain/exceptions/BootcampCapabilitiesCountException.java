package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class BootcampCapabilitiesCountException extends BootcampException {
    public BootcampCapabilitiesCountException(String message) {
        super(ExceptionStatusCode.BAD_REQUEST, message, 400);
    }
}

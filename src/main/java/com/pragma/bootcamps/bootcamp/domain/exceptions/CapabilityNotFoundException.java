package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class CapabilityNotFoundException extends BootcampException {

    public CapabilityNotFoundException(String message) {
        super(ExceptionStatusCode.NOT_FOUND, message, 404);
    }

}

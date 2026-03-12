package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class RepeatedCapabilitiesException extends BootcampException {
    public RepeatedCapabilitiesException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }
}

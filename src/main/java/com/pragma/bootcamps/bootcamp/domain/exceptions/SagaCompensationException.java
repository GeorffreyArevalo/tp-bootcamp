package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class SagaCompensationException extends BootcampException {
    public SagaCompensationException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }
}

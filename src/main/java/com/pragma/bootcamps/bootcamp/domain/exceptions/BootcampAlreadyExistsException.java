package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class BootcampAlreadyExistsException extends BootcampException {

    public BootcampAlreadyExistsException(String message) {
        super(ExceptionStatusCode.CONFLICT, message, 409);
    }

}

package com.pragma.bootcamps.bootcamp.domain.exceptions;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;

public class CapabilityMicroserviceException extends BootcampException {

    public CapabilityMicroserviceException(String message) {
        super(ExceptionStatusCode.INTERNAL_SERVER_ERROR, message, 500);
    }

}

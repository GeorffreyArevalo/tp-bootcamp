package com.pragma.bootcamps.bootcamp.domain.enums;

import lombok.Getter;

@Getter
public enum ExceptionMessages {

    BOOTCAMP_CAPABILITIES_COUNT_INVALID("A bootcamp must have between 1 and 4 associated capabilities."),
    BOOTCAMP_CAPABILITIES_REPEATED("There are repeated capabilities in the list."),
    CAPABILITY_NOT_FOUND("Some capability IDs do not exist."),
    SAGA_COMPENSATION_ASSOCIATION_FAILURE("The bootcamp could not be associated with the selected capabilities. Please try again later."),
    BOOTCAMP_ALREADY_EXISTS("A bootcamp with the name '%s' already exists."),
    WEB_CLIENT_INTERNAL_SERVER_ERROR("Internal server error in the capability microservice.");

    private final String message;

    ExceptionMessages(String message) {
        this.message = message;
    }

    public String format(Object... args) {
        return String.format(message, args);
    }

}

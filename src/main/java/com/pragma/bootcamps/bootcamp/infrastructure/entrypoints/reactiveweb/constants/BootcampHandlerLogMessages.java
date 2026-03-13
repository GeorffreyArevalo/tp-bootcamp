package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BootcampHandlerLogMessages {
    public static final String BOOTCAMP_REQUEST_RECEIVED = "[HANDLER] Received bootcamp request: {}";
    public static final String BOOTCAMP_LIST_REQUEST = "[HANDLER] List bootcamps called with page={}, size={}, sortBy={}, order={}";
    public static final String BOOTCAMP_LIST_MAPPED = "[HANDLER] Bootcamps mapped: {}";
    public static final String BOOTCAMP_DELETE_REQUEST = "[HANDLER] Delete bootcamp called for id={}";
}

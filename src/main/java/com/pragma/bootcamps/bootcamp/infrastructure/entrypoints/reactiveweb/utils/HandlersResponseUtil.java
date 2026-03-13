package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils;

import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses.BusinessResponse;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;

@UtilityClass
public class HandlersResponseUtil {

    public static <T> BusinessResponse<T> buildBodySuccessResponse(String code, T data) {
        return BusinessResponse
                .<T>builder()
                .message("Operation successful!")
                .data(data)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static BusinessResponse buildBodyFailureResponse(String code, String message, List<String> errors) {
        return BusinessResponse
                .builder()
                .message(message)
                .errors(errors)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }

}

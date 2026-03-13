package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.exception;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.webflux.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private static final String STATUS_KEY = "status";
    private static final String BODY_KEY = "body";
    private static final int DEFAULT_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR.value();

    public GlobalExceptionHandler(ErrorAttributes errorAttributes,
                                  WebProperties webProperties,
                                  ApplicationContext applicationContext,
                                  ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageReaders(serverCodecConfigurer.getReaders());
        this.setMessageWriters(serverCodecConfigurer.getWriters());
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Map<String, Object> errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults());

        int status = (int) errorAttributes.getOrDefault(STATUS_KEY, DEFAULT_ERROR_STATUS);
        Object body = errorAttributes.get(BODY_KEY);

        Object responseBody = (body != null) ? body : errorAttributes;

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(responseBody);
    }

}

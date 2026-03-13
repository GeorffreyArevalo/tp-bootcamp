package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.bootcamp.domain.spi.BootcampPersistencePort;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.mappers.BootcampDtoMapper;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class BootcampHandler {

    private final BootcampPersistencePort bootcampPersistencePort;
    private final BootcampDtoMapper mapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveBootcamp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BootcampRequest.class)
                .flatMap(validatorUtil::validate)
                .map(mapper::toModel)
                .flatMap(bootcampPersistencePort::saveBootcamp)
                .map(mapper::toResponse)
                .flatMap(savedBootcamp -> ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(HandlersResponseUtil.buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedBootcamp))

                );
    }


}

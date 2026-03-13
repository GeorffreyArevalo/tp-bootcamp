package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.handlers;

import com.pragma.bootcamps.bootcamp.domain.api.BootcampServicePort;
import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionStatusCode;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.mappers.BootcampDtoMapper;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils.ValidatorUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

import static com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.constants.BootcampHandlerLogMessages.*;
import static com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.utils.HandlersResponseUtil.buildBodySuccessResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampHandler {

    private final BootcampServicePort bootcampServicePort;
    private final BootcampDtoMapper mapper;
    private final ValidatorUtil validatorUtil;

    public Mono<ServerResponse> listenSaveBootcamp(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(BootcampRequest.class)
                .doOnNext(bootcampRequestDto -> log.info(BOOTCAMP_REQUEST_RECEIVED, bootcampRequestDto))
                .flatMap(validatorUtil::validate)
                .map(mapper::toModel)
                .flatMap(bootcampServicePort::saveBootcamp)
                .map(mapper::toResponse)
                .flatMap(savedBootcamp -> ServerResponse.created(URI.create(""))
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.CREATED.status(), savedBootcamp))

                );
    }


    public Mono<ServerResponse> listenListBootcamps(ServerRequest request) {
        int page = Integer.parseInt(request.queryParam("page").orElse("0"));
        int size = Integer.parseInt(request.queryParam("size").orElse("10"));
        String sortBy = request.queryParam("sortBy").orElse("name");
        String order = request.queryParam("order").orElse("asc");

        log.info(BOOTCAMP_LIST_REQUEST, page, size, sortBy, order);

        return bootcampServicePort.getBootcampsWithCapabilities(page, size, sortBy, order)
                .map(mapper::toBootcampWithCapabilitiesResponse)
                .collectList()
                .doOnNext(dtoList -> log.info(BOOTCAMP_LIST_MAPPED, dtoList))
                .map(dtoList -> new PageImpl(dtoList, PageRequest.of(page, size), dtoList.size()))
                .flatMap(pageResult -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(buildBodySuccessResponse(ExceptionStatusCode.OK.status(), pageResult))
                );
    }

    public Mono<ServerResponse> listenDeleteBootcamp(ServerRequest request) {
        Long bootcampId = Long.valueOf(request.pathVariable("bootcampId"));
        log.info(BOOTCAMP_DELETE_REQUEST, bootcampId);
        return bootcampServicePort.deleteBootcamp(bootcampId)
                .then(ServerResponse.noContent().build())
                .doOnSuccess(resp -> log.info("[HANDLER] Bootcamp {} deleted successfully (cascade)", bootcampId))
                .doOnError(e -> log.error("[HANDLER] Error deleting bootcamp {}: {}", bootcampId, e.getMessage()));
    }


}

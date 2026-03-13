package com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.adapters;

import com.pragma.bootcamps.bootcamp.domain.clients.CapabilityAssociationClientPort;
import com.pragma.bootcamps.bootcamp.domain.enums.ExceptionMessages;
import com.pragma.bootcamps.bootcamp.domain.exceptions.BootcampCapabilitiesCountException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.CapabilityMicroserviceException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.CapabilityNotFoundException;
import com.pragma.bootcamps.bootcamp.domain.exceptions.RepeatedCapabilitiesException;
import com.pragma.bootcamps.bootcamp.domain.models.CapabilitySummary;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.dtos.requests.AssociationRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.dtos.responses.CapabilityListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CapabilityMicroserviceClientAdapter implements CapabilityAssociationClientPort {

    private static final String ASSOCIATE_CAPABILITIES_URL = "/capabilities/bootcamp-associations";
    private static final String GET_CAPABILITIES_URL = "/capability/botcamps/{bootcampId}/capabilities";

    @Value("${adapter.clients.clients.capability.base-url}")
    private String capabilityMicroserviceBaseUrl;

    private final WebClient client;

    public Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds) {
        return client.post()
                .uri(String.format("%s%s", capabilityMicroserviceBaseUrl,  ASSOCIATE_CAPABILITIES_URL))
                .bodyValue(new AssociationRequest(bootcampId, capabilityIds))
                .retrieve()
                .onStatus(status -> status.value() == 400, response ->
                        response.bodyToMono(String.class)
                                .flatMap(body -> Mono.error(new BootcampCapabilitiesCountException(body)))
                )
                .onStatus(status -> status.value() == 409, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new RepeatedCapabilitiesException(body)))
                )
                .onStatus(status -> status.value() == 404, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new CapabilityNotFoundException(body)))
                )
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body -> Mono.error(new RuntimeException(body)))
                )
                .toBodilessEntity()
                .then();
    }

    @Override
    public Flux<CapabilitySummary> getCapabilitiesByBootcampId(Long bootcampId) {
        return client.get()
                .uri(String.format("%s%s", capabilityMicroserviceBaseUrl, GET_CAPABILITIES_URL), bootcampId)
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response ->
                        response.bodyToMono(String.class).flatMap(body ->
                                Mono.error(new CapabilityMicroserviceException(
                                        ExceptionMessages.WEB_CLIENT_INTERNAL_SERVER_ERROR.format(body)
                                ))
                        )
                )
                .bodyToMono(CapabilityListResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.data() != null ? response.data() : List.of()));
    }


}

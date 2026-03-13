package com.pragma.bootcamps.bootcamp.domain.clients;

import com.pragma.bootcamps.bootcamp.domain.models.CapabilitySummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityAssociationClientPort {

    Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds);
    Flux<CapabilitySummary> getCapabilitiesByBootcampId(Long bootcampId);
    Mono<Void> deleteAssociatedDataByBootcampId(Long bootcampId);

}

package com.pragma.bootcamps.bootcamp.domain.clients;

import reactor.core.publisher.Mono;

import java.util.List;

public interface CapabilityAssociationClientPort {

    Mono<Void> associateCapabilities(Long bootcampId, List<Long> capabilityIds);

}

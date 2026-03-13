package com.pragma.bootcamps.bootcamp.domain.clients;

import com.pragma.bootcamps.bootcamp.domain.models.TechnologySummary;
import reactor.core.publisher.Flux;

public interface TechnologyClientPort {
    Flux<TechnologySummary> getTechnologiesByCapabilityId(Long capabilityId);
}

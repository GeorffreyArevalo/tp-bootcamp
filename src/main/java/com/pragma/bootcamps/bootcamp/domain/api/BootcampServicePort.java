package com.pragma.bootcamps.bootcamp.domain.api;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.models.BootcampWithCapabilities;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampServicePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);
    Flux<BootcampWithCapabilities> getBootcampsWithCapabilities(int page, int size, String sortBy, String order);
    Mono<Void> deleteBootcamp(Long bootcampId);
    Mono<Boolean> validateConflicts(Long newBootcampId, List<Long> enrolledBootcampIds);
    void notifyBootcampCreation(Bootcamp bootcamp);

}

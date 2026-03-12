package com.pragma.bootcamps.bootcamp.domain.spi;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampPersistencePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);
    Mono<Void> deleteBootcamp(Long bootcampId);
    Mono<Bootcamp> findBootcampByName(String name);

}

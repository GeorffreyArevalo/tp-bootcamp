package com.pragma.bootcamps.bootcamp.domain.api;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import reactor.core.publisher.Mono;

public interface BootcampServicePort {

    Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp);

}

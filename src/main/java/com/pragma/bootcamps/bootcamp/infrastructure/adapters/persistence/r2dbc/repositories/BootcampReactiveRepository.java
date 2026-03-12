package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.entities.BootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface BootcampReactiveRepository extends ReactiveCrudRepository<BootcampEntity, Long> {

    Mono<BootcampEntity> findByName(String name);

}

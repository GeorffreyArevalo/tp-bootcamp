package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.repositories;

import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.entities.BootcampEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BootcampReactiveRepository extends ReactiveCrudRepository<BootcampEntity, Long> {

    Mono<BootcampEntity> findByName(String name);
    Flux<BootcampEntity> findAllBy(Pageable pageable);
    Flux<BootcampEntity> findAllByIdIn(List<Long> bootcampIds);

}

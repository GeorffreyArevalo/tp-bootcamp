package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.adapters;


import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.spi.BootcampPersistencePort;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.mappers.BootcampEntityMapper;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.repositories.BootcampReactiveRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootcampPersistenceAdapter implements BootcampPersistencePort {

    private final BootcampReactiveRepository bootcampReactiveRepository;
    private final BootcampEntityMapper mapper;

    @Override
    public Mono<Bootcamp> saveBootcamp(Bootcamp bootcamp) {
        return bootcampReactiveRepository.save(mapper.toEntity(bootcamp))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteBootcamp(Long bootcampId) {
        return bootcampReactiveRepository.deleteById(bootcampId);
    }

    @Override
    public Mono<Bootcamp> findBootcampByName(String name) {
        return bootcampReactiveRepository.findByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Bootcamp> findBootcampById(Long bootcampId) {
        return bootcampReactiveRepository.findById(bootcampId)
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Bootcamp> findBootcampsPagedAndSorted(int page, int size, String sortBy, String order) {
        return Mono.just(order)
                .map(ord -> ord.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC)
                .map(direction -> Sort.by(direction, sortBy))
                .map(sort -> PageRequest.of(page, size, sort))
                .flatMapMany(bootcampReactiveRepository::findAllBy)
                .map(mapper::toDomain)
                .doOnNext(boc -> log.info("[DB RESULT] bootcamp_id={}, name={}, capabilityCount={}", boc.getId(), boc.getName(), boc.getCapabilityCount()));
    }
}

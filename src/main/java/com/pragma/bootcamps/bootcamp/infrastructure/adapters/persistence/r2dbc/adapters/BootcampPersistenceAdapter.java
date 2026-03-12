package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.adapters;


import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.spi.BootcampPersistencePort;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.mappers.BootcampEntityMapper;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.repositories.BootcampReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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
}

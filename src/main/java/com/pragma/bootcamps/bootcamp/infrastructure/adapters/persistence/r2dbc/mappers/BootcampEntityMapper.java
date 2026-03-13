package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.mappers;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.entities.BootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE
)
public interface BootcampEntityMapper {

    BootcampEntity toEntity(Bootcamp bootcamp);
    Bootcamp toDomain(BootcampEntity entity);

}

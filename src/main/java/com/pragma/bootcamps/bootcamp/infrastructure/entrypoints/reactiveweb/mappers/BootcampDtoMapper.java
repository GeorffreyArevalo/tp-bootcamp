package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.mappers;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.models.BootcampWithCapabilities;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests.BootcampRequest;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses.BootcampResponse;
import com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses.BootcampWithCapabilitiesResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface BootcampDtoMapper {

    Bootcamp toModel(BootcampRequest bootcampRequest);
    BootcampResponse toResponse(Bootcamp bootcamp);
    BootcampWithCapabilitiesResponse toBootcampWithCapabilitiesResponse(BootcampWithCapabilities model);

}

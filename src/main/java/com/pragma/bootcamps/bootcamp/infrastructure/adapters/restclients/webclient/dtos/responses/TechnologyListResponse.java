package com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.dtos.responses;

import com.pragma.bootcamps.bootcamp.domain.models.TechnologySummary;

import java.util.List;

public record TechnologyListResponse(
        List<TechnologySummary> data
) {
}

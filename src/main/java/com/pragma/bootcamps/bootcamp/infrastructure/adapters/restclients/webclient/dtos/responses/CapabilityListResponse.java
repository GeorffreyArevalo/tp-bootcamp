package com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.dtos.responses;

import com.pragma.bootcamps.bootcamp.domain.models.CapabilitySummary;

import java.util.List;

public record CapabilityListResponse(
        List<CapabilitySummary> data
) {
}

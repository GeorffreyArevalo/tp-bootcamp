package com.pragma.bootcamps.bootcamp.infrastructure.adapters.restclients.webclient.dtos.requests;

import java.util.List;

public record AssociationRequest(
        Long bootcampId, List<Long> capabilityIds
) {
}

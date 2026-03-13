package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests;

import java.util.List;

public record ValidateConflictRequest(
        Long newBootcampId,
        List<Long> enrolledBootcampIds
) {
}

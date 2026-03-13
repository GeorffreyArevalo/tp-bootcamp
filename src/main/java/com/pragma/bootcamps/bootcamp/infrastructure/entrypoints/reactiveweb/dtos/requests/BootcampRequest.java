package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Request DTO to create or update a bootcamp")
public record BootcampRequest(
        @NotBlank(message = "Name is required")
        @Schema(description = "Name of the bootcamp", example = "Java Bootcamp")
        String name,

        @NotBlank(message = "Description is required")
        @Schema(description = "Description of the bootcamp", example = "A bootcamp for Java developers")
        String description,

        @NotNull(message = "Release date is required")
        @Schema(description = "Release date of the bootcamp", example = "2024-01-01")
        LocalDate releaseDate,

        @NotNull(message = "Duration in days is required")
        @Schema(description = "Duration of the bootcamp in days", example = "30")
        Integer duration,

        @NotNull(message = "Capability IDs are required")
        @Schema(description = "List of capability IDs to associate", example = "[1,2,3]")
        List<Long> capabilityIds
) {
}

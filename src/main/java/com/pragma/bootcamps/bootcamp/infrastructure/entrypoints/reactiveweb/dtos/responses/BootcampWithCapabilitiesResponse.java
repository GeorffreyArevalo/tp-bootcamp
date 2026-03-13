package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "Response DTO representing a Bootcamp with capabilities and technologies")
public record BootcampWithCapabilitiesResponse(
        @Schema(description = "Unique identifier of the bootcamp", example = "1")
        Long id,

        @Schema(description = "Name of the bootcamp", example = "Java Bootcamp")
        String name,

        @Schema(description = "Description of the bootcamp", example = "A bootcamp for Java developers")
        String description,

        @Schema(description = "Release date of the bootcamp", example = "2026-01-01")
        LocalDate releaseDate,

        @Schema(description = "Duration of the bootcamp in days", example = "30")
        Integer duration,

        @Schema(description = "List of associated capabilities with technologies")
        List<CapabilitySummaryResponse> capabilities
) {
}

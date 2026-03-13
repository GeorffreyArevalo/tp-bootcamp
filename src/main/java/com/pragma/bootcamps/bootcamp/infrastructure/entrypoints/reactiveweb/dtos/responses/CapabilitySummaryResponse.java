package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Response DTO representing a Capability summary")
public record CapabilitySummaryResponse(
        @Schema(description = "Unique identifier of the capability", example = "1")
        Long id,

        @Schema(description = "Name of the capability", example = "Java")
        String name,

        @Schema(description = "List of associated technologies for this capability", example = "[{'id': 10, 'name': 'Webflux'}, {'id': 11, 'name': 'RouterFunctions'}]")
        List<TechnologySummaryResponse> technologies
) {
}

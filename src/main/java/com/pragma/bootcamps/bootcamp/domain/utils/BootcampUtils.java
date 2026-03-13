package com.pragma.bootcamps.bootcamp.domain.utils;

import com.pragma.bootcamps.bootcamp.domain.models.Bootcamp;
import com.pragma.bootcamps.bootcamp.domain.models.BootcampWithCapabilities;
import com.pragma.bootcamps.bootcamp.domain.models.CapabilitySummary;
import com.pragma.bootcamps.bootcamp.domain.models.TechnologySummary;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BootcampUtils {

    public static CapabilitySummary buildCapabilitySummaryWithTechnologies(CapabilitySummary capability, List<TechnologySummary> technologies) {
        return CapabilitySummary.builder()
                .id(capability.getId())
                .name(capability.getName())
                .technologies(technologies)
                .build();
    }

    public static BootcampWithCapabilities buildBootcampWithCapabilities(Bootcamp bootcamp, List<CapabilitySummary> capabilities) {
        return BootcampWithCapabilities.builder()
                .id(bootcamp.getId())
                .name(bootcamp.getName())
                .description(bootcamp.getDescription())
                .releaseDate(bootcamp.getReleaseDate())
                .duration(bootcamp.getDuration())
                .capabilities(capabilities)
                .build();
    }

}

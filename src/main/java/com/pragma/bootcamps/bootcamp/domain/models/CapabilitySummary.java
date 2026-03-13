package com.pragma.bootcamps.bootcamp.domain.models;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@EqualsAndHashCode
public class CapabilitySummary {
    private Long id;
    private String name;
    private List<TechnologySummary> technologies;
}

package com.pragma.bootcamps.bootcamp.domain.models;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Bootcamp {

    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer capabilityCount;
    private List<Long> capabilityIds;

}

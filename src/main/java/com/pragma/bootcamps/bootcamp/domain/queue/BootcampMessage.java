package com.pragma.bootcamps.bootcamp.domain.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootcampMessage {

    private Long bootcampId;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Integer capabilityCount;


}

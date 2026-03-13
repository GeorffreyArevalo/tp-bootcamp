package com.pragma.bootcamps.bootcamp.infrastructure.entrypoints.reactiveweb.routes.paths;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "routes.paths")
public class BootcampPath {

    private String bootcamps;
    private String bootcampsList;
    private String deleteBootcampById;
    private String validateConflicts;

}

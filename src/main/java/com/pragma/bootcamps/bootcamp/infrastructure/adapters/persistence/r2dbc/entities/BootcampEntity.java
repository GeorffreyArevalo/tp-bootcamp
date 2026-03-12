package com.pragma.bootcamps.bootcamp.infrastructure.adapters.persistence.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("bootcamp")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BootcampEntity {
    @Id
    @Column("bootcamp_id")
    private Long id;
    private String name;
    private String description;
    @Column("release_date")
    private LocalDate releaseDate;
    private Integer duration;
    @Column("capability_count")
    private Integer capabilityCount;
}

package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SlaDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String name;

    private String description;

    @NotNull
    private Integer responseTime;

    @NotNull
    private Integer resolutionTime;

    @NotNull
    private Long client;

}

package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AllStatusDTO {

    private Long id;

    @NotNull
    @Size(max = 180)
    private String name;

}

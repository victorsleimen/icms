package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ModuleSatusOrderDTO {

    private Long id;

    @NotNull
    @Size(max = 75)
    private String moduleName;

    @NotNull
    private Integer level;

    @NotNull
    private Integer order;

    @NotNull
    private Long type;

    @NotNull
    private Long status;

}

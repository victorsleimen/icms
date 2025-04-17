package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TypeDTO {

    private Long id;

    @NotNull
    @Size(max = 3)
    private String typeCode;

    @NotNull
    @Size(max = 100)
    private String typeName;

    @NotNull
    private Integer sequenceNum;

}

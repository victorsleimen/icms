package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PriorityMatrixDTO {

    private Long id;

    @NotNull
    private Integer urgency;

    @NotNull
    private Integer impact;

    @NotNull
    private Priority priority;

    @NotNull
    private Long type;

}

package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentDTO {

    private Long id;

    @NotNull
    private String description;

    @NotNull
    private Long client;

    @NotNull
    private Long user;

}

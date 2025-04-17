package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KnowledgeArticlesDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    private String content;

    @NotNull
    private Knowledgestatus status;

    @NotNull
    private Long client;

}

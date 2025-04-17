package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HistoryLogsDTO {

    private Long id;

    @NotNull
    private Entitytype entityType;

    @NotNull
    private String changedDate;

    @NotNull
    private Actiontype action;

    @NotNull
    private Long client;

}

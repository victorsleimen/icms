package com.bcom.icms.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketDTO {

    private Long id;

    @NotNull
    @Size(max = 12)
    private String code;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String description;

    private String attachment;

    @Size(max = 255)
    private String openDate;

    @NotNull
    @Size(max = 80)
    private String ticketType;

    @NotNull
    @Size(max = 75)
    private String owner;

    @NotNull
    private Status status;

    @NotNull
    private Integer urgency;

    @NotNull
    private Integer impact;

    @NotNull
    private Priority priority;

    @NotNull
    private Long client;

    @NotNull
    private Long sla;

    @NotNull
    private Long assignee;

}

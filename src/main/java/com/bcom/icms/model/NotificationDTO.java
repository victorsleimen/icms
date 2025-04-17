package com.bcom.icms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NotificationDTO {

    private Long id;

    @NotNull
    private Notificationtype notifType;

    @NotNull
    private String message;

    @NotNull
    @JsonProperty("isRead")
    private Boolean isRead;

    @NotNull
    private Long client;

    @NotNull
    private Long user;

    @NotNull
    private Long ticket;

}

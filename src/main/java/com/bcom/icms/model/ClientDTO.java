package com.bcom.icms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ClientDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String clientName;

    @NotNull
    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String tel;

    @Size(max = 20)
    private String fax;

    @Size(max = 20)
    private String mobile;

    @Size(max = 125)
    private String email;

    @Size(max = 50)
    private String registrationNum;

    @NotNull
    @Size(max = 125)
    private String webURL;

    @NotNull
    @JsonProperty("isBcom")
    private Boolean isBcom;

}

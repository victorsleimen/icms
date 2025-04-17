package com.bcom.icms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 100)
    @UserUsernameUnique
    private String username;

    @Size(max = 100)
    private String password;

    @Size(max = 100)
    private String firstName;

    @Size(max = 100)
    private String lastName;

    @Size(max = 125)
    private String email;

    private LocalDate doe;

    @Size(max = 125)
    private String timeZoneId;

    private Boolean firstLogin;

    @NotNull
    @JsonProperty("isUtc")
    private Boolean isUtc;

    @JsonProperty("isActive")
    private Boolean isActive;

    @Size(max = 100)
    private String loggedUser;

    private List<Long> roles;

    @NotNull
    private Long client;

}

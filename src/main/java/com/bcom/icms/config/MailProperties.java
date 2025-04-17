package com.bcom.icms.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class MailProperties {

    @Value("${app.mail.from}")
    private String mailFrom;

    @Value("${app.mail.displayName}")
    private String mailDisplayName;

}

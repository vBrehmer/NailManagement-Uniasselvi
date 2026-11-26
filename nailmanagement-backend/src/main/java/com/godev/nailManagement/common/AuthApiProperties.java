package com.godev.nailManagement.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "authapi")
@Getter
@Setter
public class AuthApiProperties {

    private String url;
    private String username;
    private String password;

}

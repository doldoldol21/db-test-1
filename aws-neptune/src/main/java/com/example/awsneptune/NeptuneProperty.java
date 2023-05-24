package com.example.awsneptune;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("gremlin")
@Getter
@Setter
public class NeptuneProperty {
    private String endpoint;
    private int port;
}

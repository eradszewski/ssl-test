package de.rae.demo.trustmanager.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class EnvVar {


    @Value("${server.ssl.key-store}")
    private String SSL_KEYSTORE_LOCATION;
    @Value("${server.ssl.key-store-password}")
    private String SSL_KEYSTORE_PASSWORD;

    @Value("${server.ssl.trust-store}")
    private String SSL_TRUSTSTORE_LOCATION;
    @Value("${server.ssl.trust-store-password}")
    private String SSL_TRUSTSTORE_PASSWORD;

}

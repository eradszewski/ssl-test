package de.rae.demo.trustmanager.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${app.ssl.username}")
    private String sslUsername;
    @Value("${app.ssl.password}")
    private String sslPassword;
    @Value("${app.ssl.role}")
    private String sslRole;
    /*
     * Enable x509 client authentication.
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.x509();
    }

    /*
     * Create an in-memory authentication manager. We create 1 user (localhost which
     * is the CN of the client certificate) which has a role of USER.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // TODO remove memory user and use a dedicated IDM !!
        auth.inMemoryAuthentication()
                .withUser(sslUsername)
                .password(sslPassword)
                .roles(sslRole);


    }

}

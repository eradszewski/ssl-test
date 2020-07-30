package de.rae.demo.trustmanager.controller;


import de.rae.demo.trustmanager.config.EnvVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@RestController
@RequestMapping("/client")
public class SslClientController {

    @Autowired
    RestTemplate restTemplate;

    public static final String TEST_MESSAGE_EXT = " +  extended with Message from Client";

    @GetMapping("/{hostPortEx}")
    public ResponseEntity callSslServerFromUrl(@PathVariable String hostPortEx){
        String response = restTemplate.getForObject("https://" + hostPortEx + "/server/ssl", String.class);
        assert response != null;
        return ResponseEntity.ok().body(response +  TEST_MESSAGE_EXT);

    }

}

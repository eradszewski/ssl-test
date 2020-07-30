package de.rae.demo.trustmanager.controller;


import de.rae.demo.trustmanager.config.EnvVar;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping("/server")
public class SslServerController {


    public static final String RESPONSE_MESSAGE = "Rest-Endpoint work";

    @GetMapping(path="/ssl")
    public ResponseEntity sslDummy() {
        return ResponseEntity.ok().body(RESPONSE_MESSAGE);
    }



}

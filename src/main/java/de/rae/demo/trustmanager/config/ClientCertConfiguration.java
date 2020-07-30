package de.rae.demo.trustmanager.config;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.function.Supplier;

@Configuration
public class ClientCertConfiguration {

    @Autowired
    EnvVar envVars;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) throws Exception {

        SSLContext sslContext = getSSLContext(envVars.getSSL_KEYSTORE_LOCATION(), envVars.getSSL_TRUSTSTORE_LOCATION(), envVars.getSSL_KEYSTORE_PASSWORD());
        HttpClient client = HttpClients.custom()
                .setSSLContext(sslContext)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(client);

        return new RestTemplate(requestFactory);
    }

    private SSLContext getSSLContext(String pKeyFileLocationExp, String trustStoreLocationExp, String pKeyPassword) throws Exception {

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        File pKeyFile = ResourceUtils.getFile(pKeyFileLocationExp);
        InputStream keyInput = new FileInputStream(pKeyFile);
        char[] password = pKeyPassword.toCharArray();

        if ( trustStoreLocationExp == null ){
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                }
            }};
            keyStore.load(keyInput, password);
            keyInput.close();
            keyManagerFactory.init(keyStore, password);
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(keyManagerFactory.getKeyManagers(), trustAllCerts, new SecureRandom());
            return context;

        }else{
//        Over truststore file
            File trustStoreFile = ResourceUtils.getFile(trustStoreLocationExp);
            FileInputStream trustStoreFileStream = new FileInputStream(trustStoreFile);

            return SSLContextBuilder
                    .create()
                    .loadKeyMaterial(pKeyFile, pKeyPassword.toCharArray(), pKeyPassword.toCharArray())
                    .loadTrustMaterial(trustStoreFile, pKeyPassword.toCharArray())
                    .build();

        }
    }
}

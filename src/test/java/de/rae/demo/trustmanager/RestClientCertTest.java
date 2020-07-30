package de.rae.demo.trustmanager;

import de.rae.demo.trustmanager.controller.SslClientController;
import de.rae.demo.trustmanager.controller.SslServerController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = TrustmanagerApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class RestClientCertTest {

    Logger logger = LoggerFactory.getLogger(RestClientCertTest.class);

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    SslClientController sslClientController;


    @Test
    public void is_ssl_endpoint_callable_with_direct_localhost() {
        String response = restTemplate.getForObject("https://localhost:" + port + "/server/ssl", String.class);
        assertEquals(SslServerController.RESPONSE_MESSAGE, response);
    }


    @Test
    public void is_ssl_endpoint_callable_with_client_controller_cert() throws InterruptedException {
        String testHostPort = "localhost:" + port;
        ResponseEntity<String> response = sslClientController.callSslServerFromUrl(testHostPort);
        assert response != null;
        assertEquals( SslServerController.RESPONSE_MESSAGE + SslClientController.TEST_MESSAGE_EXT,  response.getBody());
    }
}
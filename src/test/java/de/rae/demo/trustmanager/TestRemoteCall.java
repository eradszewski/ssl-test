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
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@ActiveProfiles("remotetest")
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = TrustmanagerApplication.class
)
public class TestRemoteCall {


    @Autowired
    SslClientController sslClientController;
    @Value("${app.test.host-port-ex}")
    private String testHostPort;
    @Value("${app.test.delay}")
    private int delay;

    @Test
    public void is_remote_ssl_endpoint_callable_with_client_controller_cert() throws InterruptedException {
        final Logger logger = LoggerFactory.getLogger(TestRemoteCall.class);

        logger.info("Create a Delay of "+ delay + " milSec, to call the RestEndpoint");
        Thread.sleep(delay);
        System.out.println("###############################################################################################");
        System.out.println("Remote SSL call Test executed on dest:" + testHostPort);
        System.out.println("###############################################################################################");
        ResponseEntity<String> response = sslClientController.callSslServerFromUrl(testHostPort);
        assert response != null;
        assertEquals( SslServerController.RESPONSE_MESSAGE + SslClientController.TEST_MESSAGE_EXT,  response.getBody());

    }

}

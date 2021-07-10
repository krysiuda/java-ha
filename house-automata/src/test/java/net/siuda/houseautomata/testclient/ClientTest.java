package net.siuda.houseautomata.testclient;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.Base64;

public class ClientTest {

    private final static Logger LOG = LoggerFactory.getLogger(ClientTest.class);

    @Test
    public void test() throws Exception {
        URI uri = URI.create("http://ha.dev.go.siuda.net/token/");
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest httpRequest = HttpRequest.newBuilder(uri)
                .GET()
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String token = httpResponse.body();
        LOG.debug("Response for GET: {}", token);
        byte [] tokenBytes = Base64.getDecoder().decode(token);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.writeBytes(tokenBytes);
        byteArrayOutputStream.writeBytes("secret".getBytes());
        byte [] tokenReplyBytes = digest.digest(byteArrayOutputStream.toByteArray());
        String tokenReply = Base64.getEncoder().encodeToString(tokenReplyBytes);
        HttpRequest httpRequest2 = HttpRequest.newBuilder(uri)
                .POST(HttpRequest.BodyPublishers.ofString(tokenReply))
                .header("Content-Type", "text/plain")
                .build();
        HttpResponse<String> httpResponse2 = httpClient.send(httpRequest2, HttpResponse.BodyHandlers.ofString());
        LOG.debug("Response for POST: {}", httpResponse2.body());
    }

}

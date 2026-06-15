package org.chesyon.nosepass;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.HttpEntity;

public class Client {
    private static Client currentClient;
    private CloseableHttpClient client;
    private String url;

    public static Client getCurrentClient() {
        return currentClient;
    }

    // Client constructor. There should be no more
    public Client() throws IOException {
        if (currentClient != null) {
            currentClient.stopClient();
        }
        client = HttpClientBuilder.create().build();
        url = System.getenv("SKYCOMPASS_URL");
        if (url == null)
            url = "http://localhost:8000";
        currentClient = this;
    }

    public void stopClient() throws IOException {
        client.close();
    }

    public String sendRequestStr(String requestStr) {
        HttpPost post = new HttpPost(url);
        StringEntity postingString;
        try {
            postingString = new StringEntity(requestStr);
        } catch (UnsupportedEncodingException e) { // This shouldn't ever happen
            e.printStackTrace();
            return null;
        }
        post.setEntity(postingString);
        post.setHeader("Content-type", "application/json");
        String output;
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            output = new String(content.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return output;
    }

    public CompassResponse sendRequest(CompassRequest request) {
        return CompassResponse.fromJsonStr(sendRequestStr(request.toJsonStr()));
    }
}

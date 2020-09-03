package no.phasfjo;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpDemo {

    private HttpClient client;
    private HttpRequest request;
    private HttpResponse<String> response;

    public HttpClient getClient() {
        return client;
    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(HttpRequest request) {
        this.request = request;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }

    public void setResponse(HttpResponse<String> response) {
        this.response = response;
    }

    public HttpDemo() {
    }

    public HttpDemo(HttpClient client, HttpRequest request, HttpResponse<String> response) {
        this.client = client;
        this.request = request;
        this.response = response;
    }
}

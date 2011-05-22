package de.openended.cloudurlwatcher.task;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.springframework.util.StopWatch;

public class UrlWatcherImpl implements UrlWatcher {

    private HttpClient httpClient;

    @Override
    public UrlWatchResult watchUrl(String url) throws IOException {
        UrlWatchResult result = new UrlWatchResult(url);

        HttpGet method = new HttpGet(url);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(url);
        Integer statusCode = httpClient.execute(method, new ResponseHandler<Integer>() {
            @Override
            public Integer handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                return response.getStatusLine().getStatusCode();
            }
        });
        stopWatch.stop();

        result.setStatusCode(statusCode.intValue());
        result.setResponseTimeMillis(stopWatch.getTotalTimeMillis());

        return result;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

package de.openended.cloudurlwatcher.watch;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import de.openended.cloudurlwatcher.entity.UrlWatchResult;

@Component
public class UrlWatcherImpl implements UrlWatcher {

    @Autowired
    protected HttpClient httpClient;

    protected String userAgent = "CloudUrlWatcher";

    @Override
    public UrlWatchResult watchUrl(String url) {
        final UrlWatchResult result = new UrlWatchResult(url);

        HttpGet method = createHttpGetMethod(url);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start(url);
        Integer statusCode = null;
        try {
            statusCode = httpClient.execute(method, new ResponseHandler<Integer>() {
                @Override
                public Integer handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
                    result.setStatusCode(response.getStatusLine().getStatusCode());
                    return response.getStatusLine().getStatusCode();
                }
            });
        } catch (IOException e) {
            statusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
        }
        stopWatch.stop();

        result.setStatusCode(statusCode.intValue());
        result.setResponseTimeMillis(stopWatch.getTotalTimeMillis());

        return result;
    }

    protected HttpGet createHttpGetMethod(String url) {
        HttpGet method = new HttpGet(url);
        method.removeHeaders(HTTP.USER_AGENT);
        method.addHeader(HTTP.USER_AGENT, userAgent);
        // http://hc.apache.org/httpcomponents-client-ga/tutorial/html/statemgmt.html#d4e798
        method.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
        return method;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

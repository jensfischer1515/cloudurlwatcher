package de.openended.cloudurlwatcher.web.controller;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;
import static org.apache.http.params.CoreConnectionPNames.SO_TIMEOUT;
import static org.apache.http.params.CoreProtocolPNames.WAIT_FOR_CONTINUE;

import java.io.IOException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;
import org.esxx.js.protocol.GAEConnectionManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DownloadController {

    private final int timeout = 10000;

    @RequestMapping(value = { "/download" })
    public ModelAndView download(Model model) throws IOException {
        ClientConnectionManager connectionManager = new GAEConnectionManager();
        HttpClient httpClient = new DefaultHttpClient(connectionManager);

        HttpParams params = httpClient.getParams();
        params.setIntParameter(CONNECTION_TIMEOUT, Integer.valueOf(timeout));
        params.setIntParameter(SO_TIMEOUT, Integer.valueOf(timeout));
        params.setIntParameter(WAIT_FOR_CONTINUE, Integer.valueOf(timeout));

        HttpGet method = new HttpGet("http://www.google.com");
        method.setParams(params);

        String response = httpClient.execute(method, new BasicResponseHandler());

        model.addAttribute("response", response);

        return new ModelAndView("download", model.asMap());
    }
}

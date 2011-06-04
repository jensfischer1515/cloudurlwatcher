/*
     ESXX - The friendly ECMAscript/XML Application Server
     Copyright (C) 2007-2010 Martin Blom <martin@blom.org>

     This program is free software: you can redistribute it and/or
     modify it under the terms of the GNU Lesser General Public License
     as published by the Free Software Foundation, either version 3
     of the License, or (at your option) any later version.

     This program is distributed in the hope that it will be useful,
     but WITHOUT ANY WARRANTY; without even the implied warranty of
     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
     GNU Lesser General Public License for more details.

     You should have received a copy of the GNU Lesser General Public License
     along with this program.  If not, see <http://www.gnu.org/licenses/>.


     PLEASE NOTE THAT THIS FILE'S LICENSE IS DIFFERENT FROM THE REST OF ESXX!
 */

package org.esxx.js.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLSession;

import org.apache.http.Header;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;

/**
 * 
 * @author Martin Blom <martin@blom.org>
 * @author jensfischer
 */
class GAEClientConnection implements ManagedClientConnection {

    private URLFetchService urlFetchService;

    private boolean closed;

    private ClientConnectionManager connectionManager;

    private HTTPRequest request;

    private HTTPResponse response;

    private boolean reusable;

    private HttpRoute route;

    private Object state;

    public GAEClientConnection(final ClientConnectionManager connectionManager, final HttpRoute route, final Object state,
            final URLFetchService urlFetchService) {
        super();
        this.connectionManager = connectionManager;
        this.route = route;
        this.state = state;
        this.urlFetchService = urlFetchService;
        this.closed = true;
    }

    @Override
    public void abortConnection() throws IOException {
        unmarkReusable();
        shutdown();
    }

    @Override
    public void close() throws IOException {
        request = null;
        response = null;
        closed = true;
    }

    @Override
    public void flush() throws IOException {
        if (request != null) {
            response = urlFetchService.fetch(request);
            request = null;
        } else {
            response = null;
        }
    }

    @Override
    public InetAddress getLocalAddress() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public HttpConnectionMetrics getMetrics() {
        return null;
    }

    @Override
    public InetAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getRemotePort() {
        final HttpHost host = route.getTargetHost();
        return connectionManager.getSchemeRegistry().getScheme(host).resolvePort(host.getPort());
    }

    @Override
    public HttpRoute getRoute() {
        return route;
    }

    @Override
    public int getSocketTimeout() {
        return -1;
    }

    @Override
    public SSLSession getSSLSession() {
        return null;
    }

    @Override
    public Object getState() {
        return state;
    }

    @Override
    public boolean isMarkedReusable() {
        return reusable;
    }

    @Override
    public boolean isOpen() {
        return (request != null) || (response != null);
    }

    @Override
    public boolean isResponseAvailable(final int timeout) throws IOException {
        return (response != null);
    }

    @Override
    public boolean isSecure() {
        return route.isSecure();
    }

    @Override
    public boolean isStale() {
        return !isOpen() && !closed;
    }

    @Override
    public void layerProtocol(final HttpContext context, final HttpParams params) throws IOException {
        throw new IOException("layerProtocol() not supported");
    }

    @Override
    public void markReusable() {
        reusable = true;
    }

    @Override
    public void open(final HttpRoute route, final HttpContext context, final HttpParams params) throws IOException {
        close();
        this.route = route;
    }

    @Override
    public void receiveResponseEntity(HttpResponse response) throws HttpException, IOException {
        if (this.response == null) {
            throw new IOException("receiveResponseEntity() called on closed connection");
        }

        final ByteArrayEntity bae = new ByteArrayEntity(this.response.getContent());
        bae.setContentType(response.getFirstHeader("Content-Type"));
        response.setEntity(bae);

        response = null;
    }

    @Override
    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        if (this.response == null) {
            flush();
        }

        final HttpResponse response = new BasicHttpResponse(new ProtocolVersion("HTTP", 1, 1), this.response.getResponseCode(), null);

        for (final HTTPHeader h : this.response.getHeaders()) {
            response.addHeader(h.getName(), h.getValue());
        }

        return response;
    }

    @Override
    public void releaseConnection() throws IOException {
        connectionManager.releaseConnection(this, Long.MAX_VALUE, TimeUnit.MILLISECONDS);
    }

    @Override
    public void sendRequestEntity(final HttpEntityEnclosingRequest request) throws HttpException, IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (request.getEntity() != null) {
            request.getEntity().writeTo(baos);
        }
        this.request.setPayload(baos.toByteArray());
    }

    @Override
    public void sendRequestHeader(final HttpRequest request) throws HttpException, IOException {
        try {
            final HttpHost host = route.getTargetHost();

            final URI uri = new URI(host.getSchemeName() + "://" + host.getHostName()
                    + ((host.getPort() == -1) ? "" : (":" + host.getPort())) + request.getRequestLine().getUri());

            this.request = new HTTPRequest(uri.toURL(), HTTPMethod.valueOf(request.getRequestLine().getMethod()), FetchOptions.Builder
                    .disallowTruncate().doNotFollowRedirects());
        } catch (final URISyntaxException ex) {
            throw new IOException("Malformed request URI: " + ex.getMessage(), ex);
        } catch (final IllegalArgumentException ex) {
            throw new IOException("Unsupported HTTP method: " + ex.getMessage(), ex);
        }

        for (final Header h : request.getAllHeaders()) {
            this.request.addHeader(new HTTPHeader(h.getName(), h.getValue()));
        }
    }

    @Override
    public void setIdleDuration(final long duration, final TimeUnit unit) {
        // Do nothing
    }

    @Override
    public void setSocketTimeout(final int timeout) {
    }

    @Override
    public void setState(final Object state) {
        this.state = state;
    }

    @Override
    public void shutdown() throws IOException {
        close();
    }

    @Override
    public void tunnelProxy(final HttpHost next, final boolean secure, final HttpParams params) throws IOException {
        throw new IOException("tunnelProxy() not supported");
    }

    @Override
    public void tunnelTarget(final boolean secure, final HttpParams params) throws IOException {
        throw new IOException("tunnelTarget() not supported");
    }

    @Override
    public void unmarkReusable() {
        reusable = false;
    }
}

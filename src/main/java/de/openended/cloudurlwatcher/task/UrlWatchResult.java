package de.openended.cloudurlwatcher.task;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class UrlWatchResult implements Comparable<UrlWatchResult>, Serializable {

    private static final long serialVersionUID = 799651060422411138L;

    private final long timestamp = new Date().getTime();

    private final String url;

    private int statusCode;

    private long responseTimeMillis;

    public UrlWatchResult(String url) {
        super();
        this.url = url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        UrlWatchResult that = (UrlWatchResult) obj;
        return new EqualsBuilder().append(this.url, that.url).append(this.timestamp, that.timestamp).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.url).append(this.timestamp).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("url", url).append("timestamp", timestamp)
                .append("statusCode", statusCode).append("responseTimeMillis", responseTimeMillis).toString();
    }

    @Override
    public int compareTo(UrlWatchResult that) {
        return new CompareToBuilder().append(this.url, that.url).append(this.timestamp, that.timestamp).toComparison();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getResponseTimeMillis() {
        return responseTimeMillis;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setResponseTimeMillis(long responseTimeMillis) {
        this.responseTimeMillis = responseTimeMillis;
    }
}

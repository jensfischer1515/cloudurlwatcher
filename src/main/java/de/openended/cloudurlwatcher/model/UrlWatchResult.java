package de.openended.cloudurlwatcher.model;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
@Queries({
        @Query(name = "findByUrl", value = "SELECT FROM de.openended.cloudurlwatcher.model.UrlWatchResult WHERE url == :url"),
        @Query(name = "findAfterTimestamp", value = "SELECT FROM de.openended.cloudurlwatcher.model.UrlWatchResult WHERE timestamp > :afterTimestamp"),
        @Query(name = "findBeforeTimestamp", value = "SELECT FROM de.openended.cloudurlwatcher.model.UrlWatchResult WHERE timestamp < :beforeTimestamp"),
        @Query(name = "findBetweenTimestamps", value = "SELECT FROM de.openended.cloudurlwatcher.model.UrlWatchResult WHERE timestamp > :afterTimestamp AND timestamp < :beforeTimestamp") })
public class UrlWatchResult implements Comparable<UrlWatchResult>, Model {

    private static final long serialVersionUID = 799651060422411138L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private long responseTimeMillis;

    @Persistent
    private int statusCode;

    @Persistent
    private long timestamp = new Date().getTime();

    @Persistent
    private String url;

    public UrlWatchResult() {
        super();
    }

    public UrlWatchResult(String url) {
        this();
        this.url = url;
    }

    @Override
    public int compareTo(UrlWatchResult that) {
        return new CompareToBuilder().append(this.url, that.url).append(this.timestamp, that.timestamp).toComparison();
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
    public Long getId() {
        return id;
    }

    public long getResponseTimeMillis() {
        return responseTimeMillis;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.url).append(this.timestamp).toHashCode();
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setResponseTimeMillis(long responseTimeMillis) {
        this.responseTimeMillis = responseTimeMillis;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("id", id).append("url", url)
                .append("timestamp", timestamp).append("statusCode", statusCode).append("responseTimeMillis", responseTimeMillis)
                .toString();
    }
}

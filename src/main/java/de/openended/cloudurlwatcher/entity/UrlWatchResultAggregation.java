package de.openended.cloudurlwatcher.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;

import de.openended.cloudurlwatcher.cron.Schedule;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
@Queries({ @Query(name = "findByUrlAndAggregationNameBetweenTimestamps", value = "SELECT FROM de.openended.cloudurlwatcher.entity.UrlWatchResultAggregation WHERE url == :url AND aggregationName == :aggregationName AND minTimestamp >= :afterTimestamp") })
public class UrlWatchResultAggregation implements Entity {

    private static final long serialVersionUID = 8924341607922074292L;

    public static abstract class StatusCodeEmbedded {
        @Persistent
        protected int statusCode;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }
    }

    @PersistenceCapable
    @EmbeddedOnly
    public static class StatusCodeCount extends StatusCodeEmbedded implements Serializable {

        private static final long serialVersionUID = -751956095622674909L;

        @Persistent
        private long count;

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    @PersistenceCapable
    @EmbeddedOnly
    public static class ResponseTime extends StatusCodeEmbedded implements Serializable {
        private static final long serialVersionUID = -3187766819477331700L;

        @Persistent
        private double responseTime;

        public double getResponseTime() {
            return responseTime;
        }

        public void setResponseTime(double responseTime) {
            this.responseTime = responseTime;
        }
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String url;

    @Persistent
    private String aggregationName;

    @Persistent
    private long aggregateCount;

    @Persistent
    private long maxTimestamp = Long.MIN_VALUE;

    @Persistent
    private long minTimestamp = Long.MAX_VALUE;

    @Persistent
    private List<ResponseTime> avgResponseTimes = new ArrayList<ResponseTime>();

    // @Persistent
    private List<ResponseTime> maxResponseTimes = new ArrayList<ResponseTime>();

    // @Persistent
    private List<ResponseTime> minResponseTimes = new ArrayList<ResponseTime>();

    @Persistent
    private List<StatusCodeCount> statusCodeCounts = new ArrayList<StatusCodeCount>();

    public UrlWatchResultAggregation(String url) {
        super();
        this.url = url;
    }

    public void aggregateUrlWatchResults(Collection<UrlWatchResult> urlWatchResults) {
        for (UrlWatchResult urlWatchResult : urlWatchResults) {
            this.aggregateUrlWatchResult(urlWatchResult);
        }
    }

    public synchronized void aggregateUrlWatchResult(UrlWatchResult urlWatchResult) {
        processUrl(urlWatchResult.getUrl());
        processMinTimestamp(urlWatchResult.getTimestamp());
        procesMaxTimestamp(urlWatchResult.getTimestamp());
        processStatusCodeCounts(urlWatchResult.getStatusCode());
        processMinResponseTimes(urlWatchResult.getStatusCode(), urlWatchResult.getResponseTimeMillis());
        processMaxResponseTimes(urlWatchResult.getStatusCode(), urlWatchResult.getResponseTimeMillis());
        processAvgResponseTimes(urlWatchResult.getStatusCode(), urlWatchResult.getResponseTimeMillis());
        this.aggregateCount++;
    }

    protected void procesMaxTimestamp(long timestamp) {
        this.maxTimestamp = Math.max(this.maxTimestamp, timestamp);
    }

    protected void processMinTimestamp(long timestamp) {
        this.minTimestamp = Math.min(this.minTimestamp, timestamp);
    }

    protected void processStatusCodeCounts(int statusCode) {
        for (StatusCodeCount statusCodeCount : getStatusCodeCounts()) {
            if (statusCodeCount.getStatusCode() == statusCode) {
                statusCodeCount.setCount(statusCodeCount.getCount() + 1);
                return;
            }
        }
        StatusCodeCount statusCodeCount = new StatusCodeCount();
        statusCodeCount.setStatusCode(statusCode);
        statusCodeCount.setCount(1);
        getStatusCodeCounts().add(statusCodeCount);
    }

    protected void processUrl(String url) {
        if (this.url == null || this.url.equals(url)) {
            this.url = url;
        } else {
            String error = String.format("Provided URL '%s' does not match this URL '%s'", url, this.url);
            throw new IllegalArgumentException(error);
        }
    }

    protected void processMinResponseTimes(int statusCode, long responseTimeMillis) {
        for (ResponseTime responseTime : getMinResponseTimes()) {
            if (responseTime.getStatusCode() == statusCode) {
                responseTime.setResponseTime(Math.min(responseTime.getResponseTime(), responseTimeMillis));
                return;
            }
        }
        ResponseTime minRespinseTime = new ResponseTime();
        minRespinseTime.setStatusCode(statusCode);
        minRespinseTime.setResponseTime(responseTimeMillis);
        getMinResponseTimes().add(minRespinseTime);
    }

    protected void processMaxResponseTimes(int statusCode, long responseTimeMillis) {
        for (ResponseTime responseTime : getMaxResponseTimes()) {
            if (responseTime.getStatusCode() == statusCode) {
                responseTime.setResponseTime(Math.max(responseTime.getResponseTime(), responseTimeMillis));
                return;
            }
        }
        ResponseTime maxResponseTime = new ResponseTime();
        maxResponseTime.setStatusCode(statusCode);
        maxResponseTime.setResponseTime(responseTimeMillis);
        getMaxResponseTimes().add(maxResponseTime);
    }

    protected void processAvgResponseTimes(int statusCode, long responseTimeMillis) {
        for (ResponseTime responseTime : getAvgResponseTimes()) {
            if (responseTime.getStatusCode() == statusCode) {
                double totalResponseTime = (responseTime.getResponseTime() * getAggregateCount()) + responseTimeMillis;
                responseTime.setResponseTime(totalResponseTime / (getAggregateCount() + 1));
                return;
            }
        }
        ResponseTime avgResponseTime = new ResponseTime();
        avgResponseTime.setStatusCode(statusCode);
        avgResponseTime.setResponseTime(responseTimeMillis);
        getAvgResponseTimes().add(avgResponseTime);
    }

    public List<ResponseTime> getAvgResponseTimes() {
        return avgResponseTimes;
    }

    @Override
    public Long getId() {
        return id;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public long getMinTimestamp() {
        return minTimestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setAvgResponseTimes(List<ResponseTime> avgResponseTimes) {
        this.avgResponseTimes = avgResponseTimes;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public void setMinTimestamp(long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Schedule getAggregation() {
        return Schedule.valueOf(aggregationName);
    }

    public String getAggregationName() {
        return aggregationName;
    }

    public void setAggregationName(String aggregationName) {
        this.aggregationName = aggregationName;
    }

    public long getAggregateCount() {
        return aggregateCount;
    }

    public void setAggregateCount(long aggregateCount) {
        this.aggregateCount = aggregateCount;
    }

    public List<StatusCodeCount> getStatusCodeCounts() {
        return statusCodeCounts;
    }

    public void setStatusCodeCounts(List<StatusCodeCount> statusCodeCounts) {
        this.statusCodeCounts = statusCodeCounts;
    }

    public List<ResponseTime> getMaxResponseTimes() {
        return maxResponseTimes;
    }

    public void setMaxResponseTimes(List<ResponseTime> maxResponseTimes) {
        this.maxResponseTimes = maxResponseTimes;
    }

    public List<ResponseTime> getMinResponseTimes() {
        return minResponseTimes;
    }

    public void setMinResponseTimes(List<ResponseTime> minResponseTimes) {
        this.minResponseTimes = minResponseTimes;
    }

}

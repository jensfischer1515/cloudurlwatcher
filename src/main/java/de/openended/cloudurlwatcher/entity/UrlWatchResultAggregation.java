package de.openended.cloudurlwatcher.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import de.openended.cloudurlwatcher.cron.Schedule;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class UrlWatchResultAggregation implements Entity {

    private static final long serialVersionUID = 8924341607922074292L;

    @PersistenceCapable
    @EmbeddedOnly
    public static class StatusCodeCount implements Serializable {

        private static final long serialVersionUID = -751956095622674909L;

        @Persistent
        private int statusCode;

        @Persistent
        private long count;

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String url;

    // @Persistent
    private Schedule aggregation;

    @Persistent
    private long aggregateCount;

    @Persistent
    private long maxTimestamp = Long.MIN_VALUE;

    @Persistent
    private long minTimestamp = Long.MAX_VALUE;

    @Persistent
    private Map<Integer, Double> avgResponseTimes = new HashMap<Integer, Double>();

    // @Persistent
    private Map<Integer, Long> maxResponseTimes = new HashMap<Integer, Long>();

    // @Persistent
    private Map<Integer, Long> minResponseTimes = new HashMap<Integer, Long>();

    @Persistent
    // @Embedded
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
        long minResponseTime = minResponseTimes.containsKey(statusCode) ? minResponseTimes.get(statusCode) : Long.MAX_VALUE;
        minResponseTimes.put(statusCode, Math.min(minResponseTime, responseTimeMillis));
    }

    protected void processMaxResponseTimes(int statusCode, long responseTimeMillis) {
        long maxResponseTime = maxResponseTimes.containsKey(statusCode) ? maxResponseTimes.get(statusCode) : Long.MIN_VALUE;
        maxResponseTimes.put(statusCode, Math.max(maxResponseTime, responseTimeMillis));
    }

    protected void processAvgResponseTimes(int statusCode, long responseTimeMillis) {
        double avgResponseTime = avgResponseTimes.containsKey(statusCode) ? avgResponseTimes.get(statusCode) : responseTimeMillis;
        double totalResponseTime = (avgResponseTime * this.aggregateCount) + responseTimeMillis;
        avgResponseTimes.put(statusCode, (totalResponseTime / (this.aggregateCount + 1)));
    }

    public Map<Integer, Double> getAvgResponseTimes() {
        return avgResponseTimes;
    }

    @Override
    public Long getId() {
        return id;
    }

    public Map<Integer, Long> getMaxResponseTimes() {
        return maxResponseTimes;
    }

    public long getMaxTimestamp() {
        return maxTimestamp;
    }

    public Map<Integer, Long> getMinResponseTimes() {
        return minResponseTimes;
    }

    public long getMinTimestamp() {
        return minTimestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setAvgResponseTimes(Map<Integer, Double> avgResponseTimes) {
        this.avgResponseTimes = avgResponseTimes;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public void setMaxResponseTimes(Map<Integer, Long> maxResponseTimes) {
        this.maxResponseTimes = maxResponseTimes;
    }

    public void setMaxTimestamp(long maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public void setMinResponseTimes(Map<Integer, Long> minResponseTimes) {
        this.minResponseTimes = minResponseTimes;
    }

    public void setMinTimestamp(long minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Schedule getAggregation() {
        return aggregation;
    }

    public void setAggregation(Schedule aggregation) {
        this.aggregation = aggregation;
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
}

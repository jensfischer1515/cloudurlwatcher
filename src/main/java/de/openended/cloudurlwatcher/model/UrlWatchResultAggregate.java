package de.openended.cloudurlwatcher.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class UrlWatchResultAggregate implements Serializable {

    private static final long serialVersionUID = 8924341607922074292L;

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;

    private String url;

    private Date minTimestamp;

    private Date maxTimestamp;

    private Map<Integer, Long> statusCounts;

    private Map<Integer, Long> minResponseTimes;

    private Map<Integer, Long> maxResponseTimes;

    private Map<Integer, Double> avgResponseTimes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getMinTimestamp() {
        return minTimestamp;
    }

    public void setMinTimestamp(Date minTimestamp) {
        this.minTimestamp = minTimestamp;
    }

    public Date getMaxTimestamp() {
        return maxTimestamp;
    }

    public void setMaxTimestamp(Date maxTimestamp) {
        this.maxTimestamp = maxTimestamp;
    }

    public Map<Integer, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<Integer, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Map<Integer, Long> getMinResponseTimes() {
        return minResponseTimes;
    }

    public void setMinResponseTimes(Map<Integer, Long> minResponseTimes) {
        this.minResponseTimes = minResponseTimes;
    }

    public Map<Integer, Long> getMaxResponseTimes() {
        return maxResponseTimes;
    }

    public void setMaxResponseTimes(Map<Integer, Long> maxResponseTimes) {
        this.maxResponseTimes = maxResponseTimes;
    }

    public Map<Integer, Double> getAvgResponseTimes() {
        return avgResponseTimes;
    }

    public void setAvgResponseTimes(Map<Integer, Double> avgResponseTimes) {
        this.avgResponseTimes = avgResponseTimes;
    }
}

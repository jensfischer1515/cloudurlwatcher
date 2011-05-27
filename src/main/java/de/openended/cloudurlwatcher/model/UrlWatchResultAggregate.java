package de.openended.cloudurlwatcher.model;

import java.io.Serializable;
import java.util.Date;

public class UrlWatchResultAggregate implements Serializable {

    private static final long serialVersionUID = 8924341607922074292L;

    public String url;

    public Long count;

    public Long minResponseTime;

    public Long maxResponseTime;

    public Double avgResponseTime;

    public Date minTimestamp;

    public Date maxTimestamp;
}

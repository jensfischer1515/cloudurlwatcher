package de.openended.cloudurlwatcher.service;

import org.joda.time.Interval;

import de.openended.cloudurlwatcher.cron.Schedule;
import de.openended.cloudurlwatcher.entity.UrlWatchResult;
import de.openended.cloudurlwatcher.entity.UrlWatchResultAggregation;

public interface UrlWatchService {

    UrlWatchResult watchUrl(String url);

    UrlWatchResultAggregation aggregateUrlWatchResults(String url, Interval interval, Schedule aggregation);
}

package de.openended.cloudurlwatcher.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.openended.cloudurlwatcher.cron.Schedule;
import de.openended.cloudurlwatcher.entity.UrlWatchResult;
import de.openended.cloudurlwatcher.entity.UrlWatchResultAggregation;
import de.openended.cloudurlwatcher.repository.GenericRepository;
import de.openended.cloudurlwatcher.watch.UrlWatcher;

@Service
public class UrlWatchServiceImpl implements UrlWatchService {

    private static final Logger logger = LoggerFactory.getLogger(UrlWatchServiceImpl.class);

    @Autowired
    protected UrlWatcher urlWatcher;

    @Autowired
    protected GenericRepository repository;

    @Override
    public UrlWatchResult watchUrl(String url) {
        UrlWatchResult entity = repository.save(urlWatcher.watchUrl(url));
        logger.info("Saved entity with id {}", entity.getId());
        return entity;
    }

    @Override
    public UrlWatchResultAggregation aggregateUrlWatchResults(String url, Interval interval, Schedule aggregation) {
        UrlWatchResultAggregation urlWatchResultAggregation = new UrlWatchResultAggregation(url);
        urlWatchResultAggregation.setAggregationName(aggregation.name());

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("url", url);
        parameters.put("afterTimestamp", interval.getStartMillis());
        parameters.put("beforeTimestamp", interval.getEndMillis());

        Collection<UrlWatchResult> urlWatchResults = repository.findByNamedQuery(UrlWatchResult.class, "findByUrlBetweenTimestamps",
                parameters);
        urlWatchResultAggregation.aggregateUrlWatchResults(urlWatchResults);

        switch (aggregation) {
        case MINUTELY:
            // do stuff based on UrlWatchResults
            break;
        case HOURLY:
        case DAILY:
        case WEEKLY:
        case MONTHLY:
        case YEARLY:
            // do different stuff based on UrlWatchResultAggregations
            break;
        }

        urlWatchResultAggregation = repository.save(urlWatchResultAggregation);
        return urlWatchResultAggregation;
    }
}

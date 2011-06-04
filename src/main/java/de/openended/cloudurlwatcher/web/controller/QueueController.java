package de.openended.cloudurlwatcher.web.controller;

import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import de.openended.cloudurlwatcher.entity.UrlWatchResult;
import de.openended.cloudurlwatcher.service.Headers;
import de.openended.cloudurlwatcher.service.UrlWatchService;

/**
 * You can examine and manipulate tasks from the developer console at:
 * http://localhost:8080/_ah/admin/taskqueue.
 * 
 * @author jfischer
 * 
 */
@Controller
@RequestMapping(value = { "/_ah" + QueueController.PATH_PREFIX, QueueController.PATH_PREFIX }, method = RequestMethod.POST)
public class QueueController {
    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);

    public static final String PATH_PREFIX = "/queue";

    @Autowired
    private UrlWatchService urlWatchService;

    @RequestMapping(value = { "/watchUrl" })
    @ResponseBody
    public String watchUrl(@RequestHeader(value = Headers.WATCH_URL, required = true) String watchUrl,
            @RequestHeader(value = Headers.QUEUE_NAME, required = false) String queueName,
            @RequestHeader(value = Headers.TASK_NAME, required = false) String taskName,
            @RequestHeader(value = Headers.TASK_RETRY_COUNT, required = false) String taskRetryCount,
            @RequestHeader(value = Headers.FAIL_FAST, required = false) String failFast) {
        logger.info("Watching URL '{}'", watchUrl);
        UrlWatchResult result = urlWatchService.watchUrl(watchUrl);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/aggregateUrl" })
    @ResponseBody
    public String aggregateUrl(@RequestHeader(value = Headers.WATCH_URL, required = true) String watchUrl,
            @RequestHeader(value = Headers.WATCH_AGGREGATION, required = true) String watchAggregation,
            @RequestHeader(value = Headers.WATCH_AGGREGATE_FROM, required = true) long watchAggregateFrom,
            @RequestHeader(value = Headers.WATCH_AGGREGATE_TO, required = true) long watchAggregateTo,
            @RequestHeader(value = Headers.QUEUE_NAME, required = false) String queueName,
            @RequestHeader(value = Headers.TASK_NAME, required = false) String taskName,
            @RequestHeader(value = Headers.TASK_RETRY_COUNT, required = false) String taskRetryCount,
            @RequestHeader(value = Headers.FAIL_FAST, required = false) String failFast) {
        logger.info("Aggregating URL '{}' for {} between {} and {}", new Object[] { watchUrl, watchAggregation, watchAggregateFrom,
                watchAggregateTo });

        Interval interval = new Interval(watchAggregateFrom, watchAggregateTo);

        // urlWatchService.aggregateUrlWatchResults(url, interval,
        // Schedule.HOURLY);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

}

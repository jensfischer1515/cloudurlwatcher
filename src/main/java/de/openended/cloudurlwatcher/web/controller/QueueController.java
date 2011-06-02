package de.openended.cloudurlwatcher.web.controller;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @RequestMapping(value = { "/MINUTELY" })
    @ResponseBody
    public String executeQueueTaskMinutely(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        urlWatchService.watchUrl(url);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/HOURLY" })
    @ResponseBody
    public String executeQueueTaskHourly(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        DateTime end = new DateTime();
        DateTime start = end.minusHours(1);
        Interval interval = new Interval(start, end);
        // urlWatchService.aggregateUrlWatchResults(url, interval,
        // Schedule.HOURLY);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/DAILY" })
    @ResponseBody
    public String executeQueueTaskDaily(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/WEEKLY" })
    @ResponseBody
    public String executeQueueTaskWeekly(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/MONTHLY" })
    @ResponseBody
    public String executeQueueTaskMonthly(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }

    @RequestMapping(value = { "/YEARLY" })
    @ResponseBody
    public String executeQueueTaskYearly(@RequestHeader(value = CronJobController.HEADER_URL, required = true) String url,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {
        logger.info("Watching URL '{}'", url);
        return String.format("Executed task '%s' from queue '%s'", taskName, queueName);
    }
}

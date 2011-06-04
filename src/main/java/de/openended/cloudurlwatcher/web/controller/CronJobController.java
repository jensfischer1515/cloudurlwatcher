package de.openended.cloudurlwatcher.web.controller;

import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.joda.time.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;

import de.openended.cloudurlwatcher.cron.Schedule;
import de.openended.cloudurlwatcher.service.Headers;

@Controller
@RequestMapping(value = { "/cron" })
public class CronJobController {
    private static final Logger logger = LoggerFactory.getLogger(CronJobController.class);

    /** TODO dynamically fetch from Datastore */
    @Resource(name = "urlsToWatch")
    private Set<String> urlsToWatch;

    @Resource(name = "scheduleToQueueMapping")
    private Map<Schedule, String> scheduleToQueueMapping;

    @RequestMapping(value = { "/{cronJob}" })
    @ResponseBody
    public String executeCronJob(@PathVariable String cronJob) {
        logger.info("Executing cron job '{}'", cronJob);
        Schedule schedule = Schedule.valueOf(cronJob);
        String queueName = findQueueName(schedule);
        Queue queue = QueueFactory.getQueue(queueName);
        for (String urlToWatch : urlsToWatch) {
            TaskOptions taskOptions = createTaskOptions(schedule, urlToWatch);
            TaskHandle handle = queue.add(taskOptions);
            logger.info("Added task '{}' to queue '{}'", handle.getName(), handle.getQueueName());
        }
        return String.format("Executed cron job '%s'", cronJob);
    }

    protected TaskOptions createTaskOptions(Schedule schedule, String urlToWatch) {
        Interval interval = schedule.getInterval();
        TaskOptions taskOptions = TaskOptions.Builder.withDefaults();
        taskOptions.url(QueueController.PATH_PREFIX + "/" + findQueueName(schedule));
        taskOptions.header(Headers.WATCH_URL, urlToWatch);
        taskOptions.header(Headers.WATCH_AGGREGATION, schedule.name());
        taskOptions.header(Headers.WATCH_AGGREGATE_FROM, String.valueOf(interval.getStartMillis()));
        taskOptions.header(Headers.WATCH_AGGREGATE_TO, String.valueOf(interval.getEndMillis()));
        return taskOptions;
    }

    protected String findQueueName(Schedule schedule) {
        logger.debug("Finding queue name for schedule {}", schedule.name());
        return (scheduleToQueueMapping.containsKey(schedule) ? scheduleToQueueMapping.get(schedule) : "default");
    }
}

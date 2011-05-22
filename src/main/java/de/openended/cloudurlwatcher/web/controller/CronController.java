package de.openended.cloudurlwatcher.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.taskqueue.TaskOptions;

import de.openended.cloudurlwatcher.cron.Schedule;

@Controller
@RequestMapping(value = { "/cron" })
public class CronController {
    private static final Logger logger = LoggerFactory.getLogger(CronController.class);

    @Resource(name = "scheduledTasks")
    private final Map<Schedule, List<TaskOptions>> scheduledTasks = new HashMap<Schedule, List<TaskOptions>>();

    @RequestMapping(value = { "/{cronJobName}" })
    @ResponseBody
    public String executeCronJob(@PathVariable String cronJobName,
            @RequestHeader(value = "X-AppEngine-Cron", defaultValue = "false") boolean header) {
        logger.info("Executing cron job '{}' with header '{}'", cronJobName, header);

        Schedule schedule = getSchedule(cronJobName);

        Queue queue = QueueFactory.getDefaultQueue();
        if (scheduledTasks != null && scheduledTasks.containsKey(schedule)) {
            for (TaskOptions task : scheduledTasks.get(schedule)) {
                TaskHandle handle = queue.add(task);
                logger.info("Added task '{}' to queue '{}'", handle.getName(), handle.getQueueName());
            }
        }
        return String.format("Executed cron job '%s'", cronJobName);
    }

    protected Schedule getSchedule(String cronJobName) {
        try {
            return Schedule.valueOf(cronJobName);
        } catch (IllegalArgumentException exception) {
            logger.warn("No Schedule with name '{}' defined!", cronJobName);
            return null;
        }
    }
}

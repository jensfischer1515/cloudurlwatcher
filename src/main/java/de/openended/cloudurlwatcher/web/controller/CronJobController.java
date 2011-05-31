package de.openended.cloudurlwatcher.web.controller;

import java.util.HashSet;
import java.util.Set;

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

@Controller
@RequestMapping(value = { "/cron" })
public class CronJobController {
    private static final Logger logger = LoggerFactory.getLogger(CronJobController.class);

    public static final String HEADER_URL = "X-CloudUrlWatcher-Url";

    protected Set<String> getUrls() {
        Set<String> urls = new HashSet<String>();
        urls.add("http://store.apple.com/de");
        urls.add("http://www.microsoft.com");
        urls.add("http://www.facebook.com");
        return urls;
    }

    @RequestMapping(value = { "/{cronJob}" })
    @ResponseBody
    public String executeCronJob(@PathVariable String cronJob) {
        logger.info("Executing cron job '{}'", cronJob);
        Queue queue = QueueFactory.getQueue(cronJob);
        for (String url : getUrls()) {
            TaskOptions taskOptions = TaskOptions.Builder.withDefaults();
            taskOptions.url(QueueController.PATH_PREFIX + "/" + cronJob);
            taskOptions.header(HEADER_URL, url);
            TaskHandle handle = queue.add(taskOptions);
            logger.info("Added task '{}' to queue '{}'", handle.getName(), handle.getQueueName());
        }
        return String.format("Executed cron job '%s'", cronJob);
    }
}

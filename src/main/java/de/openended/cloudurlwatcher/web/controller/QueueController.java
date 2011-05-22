package de.openended.cloudurlwatcher.web.controller;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * You can examine and manipulate tasks from the developer console at:
 * http://localhost:8080/_ah/admin/taskqueue.
 * 
 * @author jfischer
 * 
 */
@Controller
@RequestMapping(value = { "/_ah/queue", "/queue" })
public class QueueController {
    private static final Logger logger = LoggerFactory.getLogger(QueueController.class);

    /**
     * 
     * @param webHookName
     * @param queueName
     *            the name of the queue (possibly default)
     * @param taskName
     *            he name of the task, or a system-generated unique ID if no
     *            name was specified
     * @param taskRetryCount
     *            the number of times this task has been retried; for the first
     *            attempt, this value is 0
     * @param failFast
     *            specifies that a task running on a backend fails immediately
     *            instead of waiting in a pending queue.
     * @return
     */
    @RequestMapping(value = { "/{webHookName}" })
    @ResponseBody
    public String executeQueueTask(@PathVariable String webHookName, WebRequest webRequest,
            @RequestHeader(value = "X-AppEngine-QueueName", required = false) String queueName,
            @RequestHeader(value = "X-AppEngine-TaskName", required = false) String taskName,
            @RequestHeader(value = "X-AppEngine-TaskRetryCount", required = false) String taskRetryCount,
            @RequestHeader(value = "X-AppEngine-FailFast", required = false) String failFast) {

        // do some stuff!
        logger.info("Executing task '{}' form queue '{}' triggered by '{}'", new Object[] { taskName, queueName, webHookName });
        for (Iterator<String> headerNames = webRequest.getHeaderNames(); headerNames.hasNext();) {
            String headerName = headerNames.next();
            logger.debug("Header: {} = {}", headerName, webRequest.getHeader(headerName));
        }
        for (Iterator<String> paramNames = webRequest.getParameterNames(); paramNames.hasNext();) {
            String paramName = paramNames.next();
            logger.debug("Param: {} = {}", paramName, webRequest.getParameter(paramName));
        }

        return String.format("Executed task '%s'", taskName);
    }
}

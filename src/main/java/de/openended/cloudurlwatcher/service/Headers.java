package de.openended.cloudurlwatcher.service;

public interface Headers {
    String PREFIX = "X-CloudUrlWatcher-";

    String GAE_PREFIX = "X-AppEngine-";

    String WATCH_URL = PREFIX + "WatchUrl";

    String WATCH_AGGREGATION = PREFIX + "WatchAggregation";

    String WATCH_AGGREGATE_FROM = PREFIX + "WatchAggregateFrom";

    String WATCH_AGGREGATE_TO = PREFIX + "WatchAggregateTo";

    String QUEUE_NAME = GAE_PREFIX + "QueueName";

    String TASK_NAME = GAE_PREFIX + "TaskName";

    String TASK_RETRY_COUNT = GAE_PREFIX + "TaskRetryCount";

    String FAIL_FAST = GAE_PREFIX + "FailFast";
}

package de.openended.cloudurlwatcher.task;

import java.util.Map;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

public class TaskOptionsFactoryBean implements FactoryBean<TaskOptions>, InitializingBean {

    private long countdownMillis;

    private long etaMillis;

    private Map<String, String> headers;

    private Method method;

    private Map<String, String> params;

    private String payload;

    private RetryOptions retryOptions;

    private String taskName;

    private TaskOptions taskOptions;

    private String url;

    @Override
    public void afterPropertiesSet() throws Exception {
        boolean hasParams = (params != null);
        boolean hasPayload = (payload != null);
        Assert.state(!(hasParams & hasPayload), "Use only one: params or payload!");

        taskOptions = TaskOptions.Builder.withDefaults();

        if (taskName != null) {
            taskOptions.taskName(taskName);
        }

        if (method != null) {
            taskOptions.method(method);
        }

        if (url != null) {
            taskOptions.url(url);
        }

        if (headers != null) {
            taskOptions.headers(headers);
        }

        if (params != null) {
            for (String paramName : params.keySet()) {
                String paramValue = params.get(paramName);
                taskOptions.param(paramName, paramValue);
            }
        }

        if (payload != null) {
            taskOptions.payload(payload);
        }

        if (retryOptions != null) {
            taskOptions.retryOptions(retryOptions);
        }

        if (countdownMillis > 0) {
            taskOptions.countdownMillis(countdownMillis);
        }

        if (etaMillis > 0) {
            taskOptions.etaMillis(etaMillis);
        }
    }

    @Override
    public TaskOptions getObject() throws Exception {
        return taskOptions;
    }

    @Override
    public Class<?> getObjectType() {
        return (taskOptions != null ? taskOptions.getClass() : TaskOptions.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public void setCountdownMillis(long countdownMillis) {
        this.countdownMillis = countdownMillis;
    }

    public void setEtaMillis(long etaMillis) {
        this.etaMillis = etaMillis;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setRetryOptions(RetryOptions retryOptions) {
        this.retryOptions = retryOptions;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

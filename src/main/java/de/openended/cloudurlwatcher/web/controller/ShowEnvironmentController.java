package de.openended.cloudurlwatcher.web.controller;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ServletContextAware;

@Controller
@RequestMapping(value = { "/admin" })
public class ShowEnvironmentController implements ServletContextAware {

    private ServletContext servletContext;

    @RequestMapping(value = { "/all" })
    @ResponseBody
    public String showAll() {
        StringBuilder env = new StringBuilder();
        env.append("\n=== Context Params === \n");
        env.append(showContextParamNamesAndValues());
        env.append("\n=== System Properties === \n");
        env.append(showSystemPropertyNamesAndValues());
        env.append("\n=== Environment Variables === \n");
        env.append(showEnvironmentVariableNamesAndValues());
        return env.toString();
    }

    @RequestMapping(value = { "/contextParams" })
    @ResponseBody
    public String showContextParamNamesAndValues() {
        Map<String, String> contextParams = new HashMap<String, String>();

        @SuppressWarnings("unchecked")
        List<String> contextParamNames = Collections.list((Enumeration<String>) servletContext.getInitParameterNames());

        for (String contextParamName : contextParamNames) {
            contextParams.put(contextParamName, servletContext.getInitParameter(contextParamName));
        }
        return contextParams.toString();
    }

    @RequestMapping(value = { "/contextParam/{contextParamName}" })
    @ResponseBody
    public String showContextParamValue(@PathVariable String contextParamName) {
        return servletContext.getInitParameter(contextParamName);
    }

    @RequestMapping(value = { "/systemProperties" })
    @ResponseBody
    public String showSystemPropertyNamesAndValues() {
        return System.getProperties().toString();
    }

    @RequestMapping(value = { "/systemProperty/{systemPropertyName}" })
    @ResponseBody
    public String showSystemPropertyValue(@PathVariable String systemPropertyName) {
        return System.getProperty(systemPropertyName);
    }

    @RequestMapping(value = { "/environmentVariables" })
    @ResponseBody
    public String showEnvironmentVariableNamesAndValues() {
        return System.getenv().toString();
    }

    @RequestMapping(value = { "/environmentVariable/{environmentVariableName}" })
    @ResponseBody
    public String showEnvironmentVariableValue(@PathVariable String environmentVariableName) {
        return System.getenv(environmentVariableName);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

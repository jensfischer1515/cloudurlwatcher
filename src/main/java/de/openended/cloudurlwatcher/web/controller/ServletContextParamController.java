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
public class ServletContextParamController implements ServletContextAware {

    private ServletContext servletContext;

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

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}

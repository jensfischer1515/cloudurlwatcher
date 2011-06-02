package de.openended.cloudurlwatcher.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WarmupController {

    @RequestMapping(value = { "/_ah/warmup" })
    @ResponseBody
    public String warmup() {
        return "Application warump successful";
    }
}

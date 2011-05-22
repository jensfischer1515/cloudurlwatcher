package de.openended.cloudurlwatcher.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WaitForController {
    private final Logger logger = LoggerFactory.getLogger(WaitForController.class);

    @RequestMapping(value = { "/waitFor/{millis}" })
    @ResponseBody
    public String waitFor(@PathVariable long millis) {
        long now = System.currentTimeMillis();
        final long waitUntil = now + millis;

        while (now < waitUntil) {
            if (now % 100 == 0) {
                logger.trace("Waiting...");
            }
            now = System.currentTimeMillis();
        }
        return "OK: " + millis;
    }
}

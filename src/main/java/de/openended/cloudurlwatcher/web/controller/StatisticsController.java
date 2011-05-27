package de.openended.cloudurlwatcher.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import de.openended.cloudurlwatcher.model.UrlWatchResultAggregate;
import de.openended.cloudurlwatcher.repository.UrlWatchResultRepository;

@Controller
public class StatisticsController {

    @Autowired
    private UrlWatchResultRepository urlWatchResultRepository;

    @RequestMapping(value = "/statistics")
    public ModelAndView showStatistics(@RequestParam String url, ModelAndView modelAndView) {
        UrlWatchResultAggregate urlWatchResultAggregate = urlWatchResultRepository.findAggregateByUrl(url);
        modelAndView.addObject("urlWatchResultAggregate", urlWatchResultAggregate);
        modelAndView.setViewName("statistics");
        return modelAndView;
    }
}

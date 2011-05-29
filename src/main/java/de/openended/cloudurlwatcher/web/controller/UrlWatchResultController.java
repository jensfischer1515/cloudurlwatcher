package de.openended.cloudurlwatcher.web.controller;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import de.openended.cloudurlwatcher.model.UrlWatchResult;
import de.openended.cloudurlwatcher.repository.GenericRepository;

@Controller
@RequestMapping(value = "/api")
public class UrlWatchResultController {

    private static final String REDIRECT_AFTER_DELETE = "redirect:/api/UrlWatchResults.json";
    @Autowired
    private GenericRepository repository;

    // @ModelAttribute("statusCodes")
    public Collection<Integer> populateStatusCodes() {
        Integer[] statusCodes = new Integer[] { 200, 301, 302, 401, 403, 404, 500, 503 };
        return Arrays.asList(statusCodes);
    }

    @RequestMapping(value = { "/UrlWatchResults" }, method = RequestMethod.GET)
    public Collection<UrlWatchResult> findAll(ModelMap modelMap) {
        return repository.findAll(UrlWatchResult.class);
    }

    @RequestMapping(value = { "/UrlWatchResult/{id}" }, method = RequestMethod.GET)
    public UrlWatchResult findById(@PathVariable Long id) {
        return repository.findById(UrlWatchResult.class, id);
    }

    @RequestMapping(value = { "/UrlWatchResults/afterTimestamp/{afterTimestamp}" }, method = RequestMethod.GET)
    public Collection<UrlWatchResult> findAfterTimestamp(@PathVariable Long afterTimestamp) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("afterTimestamp", afterTimestamp);
        return repository.findByNamedQuery(UrlWatchResult.class, "findAfterTimestamp", parameters);
    }

    @RequestMapping(value = { "/UrlWatchResults" }, method = RequestMethod.DELETE)
    public String removeAll() {
        repository.removeAll(UrlWatchResult.class);
        return REDIRECT_AFTER_DELETE;
    }

    @RequestMapping(value = { "/UrlWatchResult/{id}" }, method = RequestMethod.DELETE)
    public String removeById(@PathVariable Long id) {
        repository.remove(findById(id));
        return REDIRECT_AFTER_DELETE;
    }

    @RequestMapping(value = { "/UrlWatchResults/afterTimestamp/{afterTimestamp}" }, method = RequestMethod.DELETE)
    public String removeAfterTimestamp(@PathVariable Long afterTimestamp) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("afterTimestamp", afterTimestamp);
        repository.removeByNamedQuery(UrlWatchResult.class, "findAfterTimestamp", parameters);
        return REDIRECT_AFTER_DELETE;
    }
}

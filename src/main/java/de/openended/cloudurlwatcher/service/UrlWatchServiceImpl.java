package de.openended.cloudurlwatcher.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.openended.cloudurlwatcher.model.UrlWatchResult;
import de.openended.cloudurlwatcher.repository.UrlWatchResultRepository;
import de.openended.cloudurlwatcher.watch.UrlWatcher;

@Service
public class UrlWatchServiceImpl implements UrlWatchService {

    private static final Logger logger = LoggerFactory.getLogger(UrlWatchServiceImpl.class);

    @Autowired
    protected UrlWatcher urlWatcher;

    @Autowired
    protected UrlWatchResultRepository urlWatchResultRepository;

    @Override
    public void watchUrl(String url) {
        try {
            UrlWatchResult result = urlWatcher.watchUrl(url);
            Long id = urlWatchResultRepository.create(result);
            logger.debug("Created model with id {}", id);
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }
}

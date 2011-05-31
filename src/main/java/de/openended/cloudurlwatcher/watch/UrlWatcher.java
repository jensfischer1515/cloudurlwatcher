package de.openended.cloudurlwatcher.watch;

import java.io.IOException;

import de.openended.cloudurlwatcher.entity.UrlWatchResult;

public interface UrlWatcher {

    UrlWatchResult watchUrl(String url) throws IOException;

}
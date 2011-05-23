package de.openended.cloudurlwatcher.watch;

import java.io.IOException;

import de.openended.cloudurlwatcher.model.UrlWatchResult;

public interface UrlWatcher {

    UrlWatchResult watchUrl(String url) throws IOException;

}
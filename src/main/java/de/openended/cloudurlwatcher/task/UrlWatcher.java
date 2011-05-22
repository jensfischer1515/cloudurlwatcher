package de.openended.cloudurlwatcher.task;

import java.io.IOException;

public interface UrlWatcher {

    UrlWatchResult watchUrl(String url) throws IOException;

}
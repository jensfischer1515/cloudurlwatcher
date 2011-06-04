package de.openended.cloudurlwatcher.watch;

import de.openended.cloudurlwatcher.entity.UrlWatchResult;

public interface UrlWatcher {

    UrlWatchResult watchUrl(String url);

}
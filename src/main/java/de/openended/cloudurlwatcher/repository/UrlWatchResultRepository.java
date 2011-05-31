package de.openended.cloudurlwatcher.repository;

import de.openended.cloudurlwatcher.entity.UrlWatchResultAggregation;

public interface UrlWatchResultRepository {

    UrlWatchResultAggregation findAllAggregates();

    UrlWatchResultAggregation findAggregateByUrl(String url);
}

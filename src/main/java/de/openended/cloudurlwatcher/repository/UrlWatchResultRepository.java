package de.openended.cloudurlwatcher.repository;

import de.openended.cloudurlwatcher.model.UrlWatchResultAggregate;

public interface UrlWatchResultRepository {

    UrlWatchResultAggregate findAllAggregates();

    UrlWatchResultAggregate findAggregateByUrl(String url);
}

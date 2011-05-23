package de.openended.cloudurlwatcher.repository;

import java.io.Serializable;
import java.util.Collection;

public interface Repository<M extends Serializable> {

    Collection<M> findAll();

    Long create(M result);

    void deleteById(Long id);
}

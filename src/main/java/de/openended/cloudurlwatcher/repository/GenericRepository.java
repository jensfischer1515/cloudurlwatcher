package de.openended.cloudurlwatcher.repository;

import java.util.Collection;
import java.util.Map;

import de.openended.cloudurlwatcher.entity.Entity;

public interface GenericRepository {

    <T extends Entity> T findById(Class<T> clazz, Long id);

    <T extends Entity> Collection<T> findAll(Class<T> clazz);

    <T extends Entity> Collection<T> findByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> parameters);

    <T extends Entity> void remove(T entity);

    <T extends Entity> void remove(Collection<T> entities);

    <T extends Entity> void removeAll(Class<T> clazz);

    <T extends Entity> void removeByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> parameters);

    <T extends Entity> T save(T entity);

    void flush();
}

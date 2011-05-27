package de.openended.cloudurlwatcher.repository;

import java.util.Collection;
import java.util.Map;

import de.openended.cloudurlwatcher.model.Model;

public interface GenericRepository {

    <T extends Model> T findByKey(Class<T> clazz, Long id);

    <T extends Model> Collection<T> findAll(Class<T> clazz);

    <T extends Model> void remove(T entity);

    <T extends Model> void removeAll(Class<T> clazz);

    <T extends Model> Collection<T> findByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> parameters);

    <T extends Model> T save(T entity);

    void flush();
}

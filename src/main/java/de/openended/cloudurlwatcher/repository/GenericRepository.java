package de.openended.cloudurlwatcher.repository;

import java.util.Collection;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import de.openended.cloudurlwatcher.model.Model;

public interface GenericRepository {

    <T extends Model> T findByKey(Class<T> clazz, Key id);

    <T extends Model> Collection<T> findAll(Class<T> clazz);

    <T extends Model> void remove(T domainObj);

    <T extends Model> Collection<T> findByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> values);

    <T extends Object> T save(T domainObj);

    void flush();
}
package de.openended.cloudurlwatcher.repository.jdo;

import java.util.Collection;
import java.util.Map;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ClassUtils;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import de.openended.cloudurlwatcher.model.Model;
import de.openended.cloudurlwatcher.repository.GenericRepository;

/**
 * http://agilewombat.blogspot.com/2009/12/spring-jdo-and-google-app-engine.html
 * 
 * @author Lionel Port
 * @author jfischer
 */
@Repository
@Transactional(readOnly = false)
public class GenericJdoRepository extends AbstractJdoRepository implements GenericRepository {

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> T findByKey(final Class<T> clazz, final Long id) {
        Key key = createKey(clazz, id);
        T entity = jdoTemplate.getObjectById(clazz, key);

        if (entity == null) {
            throw new ObjectRetrievalFailureException(clazz, id);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> Collection<T> findAll(final Class<T> clazz) {
        return jdoTemplate.detachCopyAll(jdoTemplate.find(clazz));
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T extends Model> void remove(final T entity) {
        Key key = createKey(entity.getClass(), entity.getId());
        T persistentEntity = (T) jdoTemplate.getObjectById(entity.getClass(), key);
        jdoTemplate.deletePersistent(persistentEntity);
    }

    @Override
    public <T extends Model> void removeAll(final Class<T> clazz) {
        jdoTemplate.deletePersistentAll(findAll(clazz));
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> Collection<T> findByNamedQuery(final Class<T> clazz, final String namedQuery,
            final Map<String, Object> parameters) {
        return jdoTemplate.findByNamedQuery(clazz, namedQuery, parameters);
    }

    @Override
    @Transactional
    public <T extends Model> T save(final T entity) {
        return jdoTemplate.makePersistent(entity);
    }

    @Override
    @Transactional
    public void flush() {
        jdoTemplate.flush();
    }

    protected <T extends Model> Key createKey(final Class<T> clazz, final Long id) {
        return KeyFactory.createKey(ClassUtils.getQualifiedName(clazz), id.longValue());
    }
}

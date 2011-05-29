package de.openended.cloudurlwatcher.repository.jdo;

import java.util.Collection;
import java.util.Map;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public <T extends Model> T findById(final Class<T> clazz, final Long id) {
        T entity = jdoTemplate.getObjectById(clazz, id);
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
        T persistentEntity = (T) jdoTemplate.getObjectById(entity.getClass(), entity.getId());
        jdoTemplate.deletePersistent(persistentEntity);
    }

    @Override
    public <T extends Model> void removeAll(final Class<T> clazz) {
        jdoTemplate.execute(new JdoCallback<Long>() {
            @Override
            public Long doInJdo(PersistenceManager pm) throws JDOException {
                Query query = pm.newQuery(clazz);
                long deleteCount = query.deletePersistentAll();
                return Long.valueOf(deleteCount);
            }
        });
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

    @Override
    public <T extends Model> void remove(Collection<T> entities) {
        for (T entity : entities) {
            remove(entity);
        }
    }

    @Override
    public <T extends Model> void removeByNamedQuery(final Class<T> clazz, final String namedQuery, final Map<String, Object> parameters) {
        jdoTemplate.execute(new JdoCallback<Long>() {
            @Override
            public Long doInJdo(PersistenceManager pm) throws JDOException {
                pm.evictAll();
                Query query = pm.newNamedQuery(clazz, namedQuery);
                long deleteCount = query.deletePersistentAll(parameters);
                return Long.valueOf(deleteCount);
            }
        });
    }
}

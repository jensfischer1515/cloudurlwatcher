package de.openended.cloudurlwatcher.repository.jdo;

import java.util.Collection;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.jdo.JdoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.appengine.api.datastore.Key;

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
public class GenericJdoRepository implements GenericRepository {

    @Autowired
    protected PersistenceManagerFactory persistenceManagerFactory;

    protected JdoTemplate jdoTemplate;

    @PostConstruct
    void createJdoTemplate() {
        jdoTemplate = new JdoTemplate(persistenceManagerFactory);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> T findByKey(Class<T> clazz, Key id) {
        T entity = jdoTemplate.getObjectById(clazz, id);

        if (entity == null) {
            throw new ObjectRetrievalFailureException(clazz, id);
        }
        return entity;
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> Collection<T> findAll(Class<T> clazz) {
        return jdoTemplate.detachCopyAll(jdoTemplate.find(clazz));
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public <T extends Model> void remove(T domainObj) {
        T domainObject = (T) jdoTemplate.getObjectById(domainObj.getClass(), domainObj.getId());
        jdoTemplate.deletePersistent(domainObject);
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Model> Collection<T> findByNamedQuery(Class<T> clazz, String namedQuery, Map<String, Object> values) {
        return jdoTemplate.findByNamedQuery(clazz, namedQuery, values);
    }

    @Override
    @Transactional
    public <T extends Object> T save(T domainObj) {
        return jdoTemplate.makePersistent(domainObj);
    }

    @Override
    @Transactional
    public void flush() {
        jdoTemplate.flush();
    }
}

package de.openended.cloudurlwatcher.repository.jdo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.Extent;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.stereotype.Repository;

import de.openended.cloudurlwatcher.model.UrlWatchResult;
import de.openended.cloudurlwatcher.repository.UrlWatchResultRepository;

@Repository
public class UrlWatchResultJDORepository implements UrlWatchResultRepository {

    protected PersistenceManagerFactory persistenceManagerFactory = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    @Override
    public Collection<UrlWatchResult> findAll() {
        PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
        try {
            List<UrlWatchResult> results = new ArrayList<UrlWatchResult>();
            Extent<UrlWatchResult> extent = persistenceManager.getExtent(UrlWatchResult.class, false);
            for (UrlWatchResult result : extent) {
                results.add(result);
            }
            extent.closeAll();

            return results;
        } finally {
            persistenceManager.close();
        }
    }

    @Override
    public Long create(UrlWatchResult result) {
        PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
        try {
            UrlWatchResult persisted = persistenceManager.makePersistent(result);
            return persisted.getId();
        } finally {
            persistenceManager.close();
        }
    }

    @Override
    public void deleteById(Long id) {
        PersistenceManager persistenceManager = persistenceManagerFactory.getPersistenceManager();
        try {
            persistenceManager.deletePersistent(persistenceManager.getObjectById(UrlWatchResult.class, id));
        } finally {
            persistenceManager.close();
        }
    }
}

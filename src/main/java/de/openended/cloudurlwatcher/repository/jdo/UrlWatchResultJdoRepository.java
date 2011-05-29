package de.openended.cloudurlwatcher.repository.jdo;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.orm.jdo.JdoCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.openended.cloudurlwatcher.model.UrlWatchResultAggregate;
import de.openended.cloudurlwatcher.repository.UrlWatchResultRepository;

@Repository
@Transactional(readOnly = false)
public class UrlWatchResultJdoRepository extends AbstractJdoRepository implements UrlWatchResultRepository {

    @Override
    public UrlWatchResultAggregate findAggregateByUrl(final String url) {
        UrlWatchResultAggregate result = jdoTemplate.execute(new JdoCallback<UrlWatchResultAggregate>() {
            @Override
            public UrlWatchResultAggregate doInJdo(PersistenceManager pm) throws JDOException {
                String jdoql = "select count(id), min(responseTimeMillis), max(responseTimeMillis), avg(responseTimeMillis) from de.openended.cloudurlwatcher.model.UrlWatchResult where url == :url";
                Query query = pm.newQuery(jdoql);
                Object[] results = (Object[]) query.execute();
                UrlWatchResultAggregate aggregate = extractAggregate(url, results);
                return aggregate;
            }

            protected UrlWatchResultAggregate extractAggregate(final String url, Object[] results) {
                UrlWatchResultAggregate aggregate = new UrlWatchResultAggregate();

                return aggregate;
            }
        });
        return result;
    }

    @Override
    public UrlWatchResultAggregate findAllAggregates() {
        // TODO Auto-generated method stub
        return null;
    }
}

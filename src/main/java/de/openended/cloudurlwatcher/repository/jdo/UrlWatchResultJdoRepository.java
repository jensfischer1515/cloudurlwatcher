package de.openended.cloudurlwatcher.repository.jdo;

import javax.jdo.JDOException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.orm.jdo.JdoCallback;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import de.openended.cloudurlwatcher.entity.UrlWatchResultAggregation;
import de.openended.cloudurlwatcher.repository.UrlWatchResultRepository;

@Repository
@Transactional(readOnly = false)
public class UrlWatchResultJdoRepository extends AbstractJdoRepository implements UrlWatchResultRepository {

    @Override
    public UrlWatchResultAggregation findAggregateByUrl(final String url) {
        UrlWatchResultAggregation result = jdoTemplate.execute(new JdoCallback<UrlWatchResultAggregation>() {
            @Override
            public UrlWatchResultAggregation doInJdo(PersistenceManager pm) throws JDOException {
                String jdoql = "select count(id), min(responseTimeMillis), max(responseTimeMillis), avg(responseTimeMillis) from de.openended.cloudurlwatcher.entity.UrlWatchResult where url == :url";
                Query query = pm.newQuery(jdoql);
                Object[] results = (Object[]) query.execute();
                UrlWatchResultAggregation aggregate = extractAggregate(url, results);
                return aggregate;
            }

            protected UrlWatchResultAggregation extractAggregate(final String url, Object[] results) {
                UrlWatchResultAggregation aggregate = new UrlWatchResultAggregation(url);

                return aggregate;
            }
        });
        return result;
    }

    @Override
    public UrlWatchResultAggregation findAllAggregates() {
        // TODO Auto-generated method stub
        return null;
    }
}

package de.openended.cloudurlwatcher.repository.jdo;

import javax.annotation.PostConstruct;
import javax.jdo.PersistenceManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jdo.JdoTemplate;

public abstract class AbstractJdoRepository {

    @Autowired
    protected PersistenceManagerFactory persistenceManagerFactory;

    protected JdoTemplate jdoTemplate;

    public AbstractJdoRepository() {
        super();
    }

    @PostConstruct
    void createJdoTemplate() {
        jdoTemplate = new JdoTemplate(persistenceManagerFactory);
    }
}
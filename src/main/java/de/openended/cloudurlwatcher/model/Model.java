package de.openended.cloudurlwatcher.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;

public interface Model extends Serializable {

    Key getId();

    void setId(Key id);
}

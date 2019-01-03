package org.apache.skywalking.oap.server.custommodule.storage.query;

import org.apache.skywalking.oap.server.library.client.Client;

/**
 * @author songmy
 */
public abstract class AbstractCustomDao<T extends Client> {

    public AbstractCustomDao(T client) {
        this.client = client;
    }

    private T client;

    public T getClient() {
        return client;
    }



}

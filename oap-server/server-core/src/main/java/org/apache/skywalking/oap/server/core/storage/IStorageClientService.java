package org.apache.skywalking.oap.server.core.storage;

import org.apache.skywalking.oap.server.library.client.Client;

/**
 * @author songmy
 */
public interface IStorageClientService<T extends Client> extends DAO {
    T obtainClient();
}

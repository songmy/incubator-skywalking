package org.apache.skywalking.oap.server.storage.plugin.elasticsearch;

import org.apache.skywalking.oap.server.core.storage.IStorageClientService;
import org.apache.skywalking.oap.server.library.client.elasticsearch.ElasticSearchClient;

/**
 * @author songmy
 */
public class StorageEsClientServiceImpl implements IStorageClientService<ElasticSearchClient> {
    private ElasticSearchClient elasticSearchClient;

    public StorageEsClientServiceImpl(ElasticSearchClient elasticSearchClient) {
        this.elasticSearchClient = elasticSearchClient;
    }

    @Override
    public ElasticSearchClient obtainClient() {
        return elasticSearchClient;
    }
}

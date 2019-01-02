package org.apache.skywalking.oap.server.storage.plugin.jdbc.h2;

import org.apache.skywalking.oap.server.core.storage.IStorageClientService;
import org.apache.skywalking.oap.server.library.client.jdbc.hikaricp.JDBCHikariCPClient;

/**
 * @author songmy
 */
public class StorageH2ClientServiceImpl implements IStorageClientService<JDBCHikariCPClient> {

    private JDBCHikariCPClient jdbcHikariCPClient;

    public StorageH2ClientServiceImpl(JDBCHikariCPClient jdbcHikariCPClient) {
        this.jdbcHikariCPClient = jdbcHikariCPClient;
    }

    @Override
    public JDBCHikariCPClient obtainClient() {
        return jdbcHikariCPClient;
    }
}

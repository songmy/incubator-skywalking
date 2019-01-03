package org.apache.skywalking.oap.server.custommodule.storage.mysql.query;

import org.apache.skywalking.oap.server.custommodule.storage.query.AbstractCustomDao;
import org.apache.skywalking.oap.server.library.client.jdbc.JDBCClientException;
import org.apache.skywalking.oap.server.library.client.jdbc.hikaricp.JDBCHikariCPClient;

import java.sql.Connection;

/**
 * @author songmy
 */
public abstract class AbstractJdbcCustomDao extends AbstractCustomDao<JDBCHikariCPClient> {
    public AbstractJdbcCustomDao(JDBCHikariCPClient client) {
        super(client);
    }

    protected Connection getConnection() throws JDBCClientException {
        return getClient().getConnection();
    }
}

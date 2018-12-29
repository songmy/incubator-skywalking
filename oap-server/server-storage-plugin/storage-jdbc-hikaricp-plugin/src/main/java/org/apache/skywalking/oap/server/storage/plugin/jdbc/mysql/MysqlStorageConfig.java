package org.apache.skywalking.oap.server.storage.plugin.jdbc.mysql;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.library.module.ModuleConfig;

import java.util.Properties;

/**
 * @author songmy
 */
@Getter
@Setter
public class MysqlStorageConfig extends ModuleConfig {
    private String jdbcUrl;
    private String user;
    private String password;
    private boolean cachePrepStmts;
    private int prepStmtCacheSize;
    private int prepStmtCacheSqlLimit;
    private boolean useServerPrepStmts;
    private boolean useLocalSessionState;
    private boolean rewriteBatchedStatements;
    private boolean cacheResultSetMetadata;
    private boolean cacheServerConfiguration;
    private boolean elideSetAutoCommits;
    private boolean maintainTimeStats;

    public Properties properties() {
        Properties properties = new Properties();
        properties.setProperty("jdbcUrl", this.getJdbcUrl());
        properties.setProperty("dataSource.user", this.getUser());
        properties.setProperty("dataSource.password", this.getPassword());
        properties.setProperty("dataSource.cachePrepStmts", this.isCachePrepStmts() + "");
        properties.setProperty("dataSource.prepStmtCacheSize", this.getPrepStmtCacheSize()+"");
        properties.setProperty("dataSource.prepStmtCacheSqlLimit", this.getPrepStmtCacheSqlLimit()+"");
        properties.setProperty("dataSource.useServerPrepStmts", this.isUseServerPrepStmts() + "");
        properties.setProperty("dataSource.useLocalSessionState", this.isUseLocalSessionState() + "");
        properties.setProperty("dataSource.rewriteBatchedStatements", this.isRewriteBatchedStatements() + "");
        properties.setProperty("dataSource.cacheResultSetMetadata", this.isCacheResultSetMetadata() + "");
        properties.setProperty("dataSource.cacheServerConfiguration", this.isCacheServerConfiguration() + "");
        properties.setProperty("dataSource.elideSetAutoCommits", this.isElideSetAutoCommits() + "");
        properties.setProperty("dataSource.maintainTimeStats", this.isMaintainTimeStats() + "");
        return properties;
    }
}

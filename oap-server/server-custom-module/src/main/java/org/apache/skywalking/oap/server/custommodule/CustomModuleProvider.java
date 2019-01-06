package org.apache.skywalking.oap.server.custommodule;

import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.server.JettyHandlerRegister;
import org.apache.skywalking.oap.server.core.storage.IStorageClientService;
import org.apache.skywalking.oap.server.core.storage.StorageModule;
import org.apache.skywalking.oap.server.custommodule.apis.CustomQueryApisJettyHandler;
import org.apache.skywalking.oap.server.custommodule.apis.ExporterJettyHandler;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.ExportMetricTimmer;
import org.apache.skywalking.oap.server.custommodule.storage.mysql.query.QueryCustomModuleDaoImpl;
import org.apache.skywalking.oap.server.custommodule.storage.query.IQueryCustomModuleDao;
import org.apache.skywalking.oap.server.library.client.Client;
import org.apache.skywalking.oap.server.library.client.jdbc.hikaricp.JDBCHikariCPClient;
import org.apache.skywalking.oap.server.library.module.*;
import org.apache.skywalking.oap.server.storage.plugin.elasticsearch.StorageModuleElasticsearchProvider;
import org.apache.skywalking.oap.server.storage.plugin.jdbc.h2.H2StorageProvider;
import org.apache.skywalking.oap.server.storage.plugin.jdbc.mysql.MySQLStorageProvider;

/**
 * @author songmy
 */
public class CustomModuleProvider extends ModuleProvider {
    @Override
    public String name() {
        return "default";
    }

    private ExporterJettyHandler exporterJettyHandler;

    @Override
    public Class<? extends ModuleDefine> module() {
        return ServerCustomModule.class;
    }

    @Override
    public ModuleConfig createConfigBeanIfAbsent() {
        return new CustomModuleConfig();
    }

    @Override
    public void prepare() throws ServiceNotProvidedException, ModuleStartException {
        exporterJettyHandler = new ExporterJettyHandler();
    }

    @Override
    public void start() throws ServiceNotProvidedException, ModuleStartException {
        JettyHandlerRegister jettyHandlerRegister = getManager().find(CoreModule.NAME).provider().getService(JettyHandlerRegister.class);
        jettyHandlerRegister.addHandler(exporterJettyHandler);
        CustomQueryApisJettyHandler customQueryApisJettyHandler = new CustomQueryApisJettyHandler(getManager());
        jettyHandlerRegister.addHandler(customQueryApisJettyHandler);
        ModuleProvider moduleProvider = (ModuleProvider) (getManager().find(StorageModule.NAME).provider());
        IQueryCustomModuleDao queryCustomModuleDao;
        String storageProviderName = moduleProvider.name();
        Client storageClient = getManager().find(StorageModule.NAME).provider().getService(IStorageClientService.class).obtainClient();
        if (MySQLStorageProvider.PROVDER_NAME.equals(storageProviderName)) {
            queryCustomModuleDao = new QueryCustomModuleDaoImpl((JDBCHikariCPClient) storageClient);
        } else if (H2StorageProvider.PROVDER_NAME.equals(storageProviderName)) {
            throw new ServiceNotProvidedException("CUSTOM_MODULLE h2自定义存储查询不支持");
        } else if (StorageModuleElasticsearchProvider.PROVDER_NAME.equals(storageProviderName)) {
            throw new ServiceNotProvidedException("CUSTOM_MODULLE elasticsearch 自定义存储查询不支持");
        } else {
            throw new ServiceNotProvidedException("CUSTOM_MODULLE " + storageProviderName + " 自定义存储查询不支持");
        }
        this.registerServiceImplementation(IQueryCustomModuleDao.class, queryCustomModuleDao);
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException, ModuleStartException {
        ExportMetricTimmer.INSTANCE.start(getManager());
    }

    @Override
    public String[] requiredModules() {
        return new String[]{CoreModule.NAME, StorageModule.NAME};
    }
}

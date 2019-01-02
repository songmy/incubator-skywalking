package org.apache.skywalking.oap.server.custommodule;

import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.server.JettyHandlerRegister;
import org.apache.skywalking.oap.server.core.storage.StorageModule;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.ExporterJettyHandler;
import org.apache.skywalking.oap.server.library.module.*;

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
    }

    @Override
    public void notifyAfterCompleted() throws ServiceNotProvidedException, ModuleStartException {

    }

    @Override
    public String[] requiredModules() {
        return new String[]{CoreModule.NAME,StorageModule.NAME};
    }
}

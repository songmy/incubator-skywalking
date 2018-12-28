package org.apache.skywalking.oap.server.prometheus.provider;

import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.server.JettyHandlerRegister;
import org.apache.skywalking.oap.server.library.module.*;
import org.apache.skywalking.oap.server.prometheus.module.PrometheusExporterModule;

/**
 * @author songmy
 */
public class PrometheusExporterProvider extends ModuleProvider {
    @Override
    public String name() {
        return "default";
    }

    private ExporterJettyHandler exporterJettyHandler;

    @Override
    public Class<? extends ModuleDefine> module() {
        return PrometheusExporterModule.class;
    }

    @Override
    public ModuleConfig createConfigBeanIfAbsent() {
        return new ExporterConfig();
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
        return new String[]{CoreModule.NAME};
    }
}

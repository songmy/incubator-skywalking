package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

import org.apache.skywalking.oap.server.library.module.ModuleManager;

/**
 * @author songmy
 */
public interface ExportMetric {
    void setModuleManager(ModuleManager moduleManager);
}

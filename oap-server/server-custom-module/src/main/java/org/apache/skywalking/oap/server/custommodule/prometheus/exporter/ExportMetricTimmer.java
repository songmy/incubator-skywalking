package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric.ServiceSlaMetric;
import org.apache.skywalking.oap.server.library.module.ModuleManager;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author songmy
 */
public enum ExportMetricTimmer {
    INSTANCE;

    public void start(ModuleManager moduleManager) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                this.metric(moduleManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 1, 9, TimeUnit.SECONDS);
    }

    private void metric(ModuleManager moduleManager) {
        ServiceSlaMetric.INSTANCE.run(moduleManager);
    }
}

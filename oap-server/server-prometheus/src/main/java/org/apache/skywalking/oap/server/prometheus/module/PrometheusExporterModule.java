package org.apache.skywalking.oap.server.prometheus.module;

import org.apache.skywalking.oap.server.library.module.ModuleDefine;

/**
 * @author songmy
 */
public class PrometheusExporterModule extends ModuleDefine {

    public static final String NAME = "Prometheus-Exporter";
    public PrometheusExporterModule() {
        super(NAME);
    }

    @Override
    public Class[] services() {
        return new Class[0];
    }
}

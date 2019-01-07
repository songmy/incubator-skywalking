package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

/**
 * @author songmy
 */
public interface IExportMetric {

    void registryMetric(ExportMetricCatalog exportMetricCatalog);

    void metricCollect(ExportMetricCatalog exportMetricCatalog);
}

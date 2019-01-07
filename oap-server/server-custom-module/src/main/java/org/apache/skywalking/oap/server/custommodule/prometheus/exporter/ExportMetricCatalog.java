package org.apache.skywalking.oap.server.custommodule.prometheus.exporter;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;
import io.prometheus.client.Summary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songmy
 */
public class ExportMetricCatalog {

    private static final String METRICPREFIX = "neunn_paas_sw";

    private CollectorRegistry collectorRegistry;

    private String metricPrefix;

    private Map<String, Object> metrics;

    public ExportMetricCatalog() {
        this(METRICPREFIX);
    }

    public ExportMetricCatalog(String metricPrefix) {
        this.metricPrefix = metricPrefix;
        metrics = new HashMap<>();
        this.collectorRegistry = new CollectorRegistry(true);
    }

    public void registryCounter(String metricName, String help, String... labelNames) {
        Counter counter = Counter.build(this.metricPrefix + metricName, help).labelNames(labelNames).register(this.collectorRegistry);
        metrics.put(metricName, counter);
    }

    public void updateCounter(String metricName, double value, String... labelValues) {
        Counter counter = (Counter) this.metrics.get(metricName);
        counter.labels(labelValues).inc(value);
    }

    public void registryGauge(String metricName, String help, String... labelNames) {
        Gauge gauge = Gauge.build(this.metricPrefix + metricName, help).labelNames(labelNames).register(this.collectorRegistry);
        metrics.put(metricName, gauge);
    }

    public void updateGauge(String metricName, double value, String... labelValues) {
        Gauge gauge = (Gauge) this.metrics.get(metricName);
        gauge.labels(labelValues).set(value);
    }


    public CollectorRegistry getCollectorRegistry() {
        return collectorRegistry;
    }

    public Summary registrySummaryTimes() {
        Summary summary = Summary.build(buildMetricName("generate_metric_spent"), "生成汇总数据消耗的时间")
                .register(this.collectorRegistry);
        metrics.put("generate_metric_spent", summary);
        return summary;
    }

    public String buildMetricName(String metricName) {
        return this.metricPrefix + "_" + metricName;
    }
}

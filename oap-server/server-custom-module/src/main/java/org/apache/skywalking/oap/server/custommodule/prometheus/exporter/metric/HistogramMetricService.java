package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric;

import io.prometheus.client.Histogram;
import org.apache.skywalking.oap.server.core.source.EndpointRelation;

/**
 * @author songmy
 */
public enum HistogramMetricService {
    INSTANCE;
    public static final Histogram HISTOGRAM = Histogram.build().name("neunn_paas_sw_request_latency_histogram").help("调用响应时间统计")
            .labelNames(CounterMetricService.segmentLabelNames)
            .buckets(0, 100, 200, 500, 1000, 1500, 2000, 2500, 3000, 3500).register();

    public void metric(EndpointRelation endpointRelation) {
        HISTOGRAM.labels(CounterMetricService.INSTANCE.labels(endpointRelation)).observe(endpointRelation.getRpcLatency());
    }
}

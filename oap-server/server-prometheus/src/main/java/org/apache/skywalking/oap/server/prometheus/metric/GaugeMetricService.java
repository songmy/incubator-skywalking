package org.apache.skywalking.oap.server.prometheus.metric;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.source.EndpointRelation;

/**
 * @author songmy
 */
public enum GaugeMetricService {
    INSTANCE;

    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_request_latency_gauge").help("调用延迟统计")
            .labelNames(CounterMetricService.segmentLabelNames).register();

    public void metric(EndpointRelation endpointRelation) {
        GAUGE.labels(CounterMetricService.INSTANCE.labels(endpointRelation)).set(endpointRelation.getRpcLatency());
    }
}

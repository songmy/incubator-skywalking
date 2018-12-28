package org.apache.skywalking.oap.server.prometheus.metric;

import io.prometheus.client.Counter;
import io.prometheus.client.SimpleCollector;
import org.apache.skywalking.oap.server.core.source.EndpointRelation;

/**
 * @author songmy
 */
public enum CounterMetricService {
    INSTANCE;

    public static final String[] segmentLabelNames = {
            "sourceServiceId",
            "sourceServiceName",
            "sourceServiceInstanceId",
            "sourceServiceInstanceName",
            "sourceEndpointId",
            "sourceEndpointName",
            "destServiceId",
            "destServiceName",
            "destServiceInstanceId",
            "destServiceInstanceName",
            "destEndpointId",
            "destEndpointName",
            "componentId",
            "latency",
            "status",
            "responseCode",
            "requestType",
            "timeBucket"
    };
    private static final Counter COUNTER = Counter.build().name("neunn_paas_sw_request_counter").help("调用次数统计")
            .labelNames(segmentLabelNames).register();

    public void increase(EndpointRelation endpointRelation) {
        COUNTER.inc();
        this.labels(COUNTER, endpointRelation).inc();
    }

    public <T extends SimpleCollector> T labels(T collector, EndpointRelation endpointRelation) {
        collector.labels(
                endpointRelation.getServiceId() + "",
                endpointRelation.getServiceName(),
                endpointRelation.getServiceInstanceId() + "",
                endpointRelation.getServiceInstanceName(),
                endpointRelation.getEndpointId() + "",
                endpointRelation.getEndpoint(),
                endpointRelation.getChildServiceId() + "",
                endpointRelation.getChildServiceName(),
                endpointRelation.getChildServiceInstanceId() + "",
                endpointRelation.getChildServiceInstanceName(),
                endpointRelation.getChildEndpointId() + "",
                endpointRelation.getChildEndpoint(),
                endpointRelation.getComponentId() + "",
                endpointRelation.getRpcLatency() + "",
                endpointRelation.isStatus() + "",
                endpointRelation.getResponseCode() + "",
                endpointRelation.getType().name(),
                endpointRelation.getTimeBucket() + ""
        );
        return collector;
    }
}

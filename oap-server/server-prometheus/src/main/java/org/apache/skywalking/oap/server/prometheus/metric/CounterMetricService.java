package org.apache.skywalking.oap.server.prometheus.metric;

import io.prometheus.client.Counter;
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
            "componentId"
    };
    private static final Counter COUNTER = Counter.build().name("neunn_paas_sw_request_counter").help("调用次数统计")
            .labelNames(segmentLabelNames).register();

    public void metric(EndpointRelation endpointRelation) {
        COUNTER.labels(this.labels(endpointRelation)).inc();
    }

    public String[] labels(EndpointRelation endpointRelation) {
        return new String[]{
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
                endpointRelation.getComponentId() + ""
        };
    }

}

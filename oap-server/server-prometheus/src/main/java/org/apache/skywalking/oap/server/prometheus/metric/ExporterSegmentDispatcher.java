package org.apache.skywalking.oap.server.prometheus.metric;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.source.EndpointRelation;

/**
 * @author songmy
 */
public class ExporterSegmentDispatcher implements SourceDispatcher<EndpointRelation> {

    @Override
    public void dispatch(EndpointRelation source) {
        CounterMetricService.INSTANCE.increase(source);
        GaugeMetricService.INSTANCE.metric(source);
    }
}

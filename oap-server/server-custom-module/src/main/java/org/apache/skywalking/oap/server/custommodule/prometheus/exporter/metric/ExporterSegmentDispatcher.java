package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.source.EndpointRelation;

/**
 * @author songmy
 */
public class ExporterSegmentDispatcher implements SourceDispatcher<EndpointRelation> {

    @Override
    public void dispatch(EndpointRelation source) {
        CounterMetricService.INSTANCE.metric(source);
        GaugeMetricService.INSTANCE.metric(source);
        HistogramMetricService.INSTANCE.metric(source);
    }
}

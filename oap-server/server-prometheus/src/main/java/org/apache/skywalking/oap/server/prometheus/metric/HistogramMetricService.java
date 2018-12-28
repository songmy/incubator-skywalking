package org.apache.skywalking.oap.server.prometheus.metric;

import io.prometheus.client.Histogram;

/**
 * @author songmy
 */
public enum HistogramMetricService {
    INSTANCE;
    public static final Histogram HISTOGRAM = Histogram.build().name("").help("")
            .labelNames(CounterMetricService.segmentLabelNames)
            .buckets(0, 100, 200, 500, 1000, 1500, 2000, 2500, 3000, 3500).register();
}

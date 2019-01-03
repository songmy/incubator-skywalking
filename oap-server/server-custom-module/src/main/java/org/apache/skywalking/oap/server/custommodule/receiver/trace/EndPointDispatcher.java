package org.apache.skywalking.oap.server.custommodule.receiver.trace;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.analysis.worker.IndicatorProcess;
import org.apache.skywalking.oap.server.core.source.Endpoint;

/**
 * @author songmy
 */
public class EndPointDispatcher implements SourceDispatcher<Endpoint> {
    @Override
    public void dispatch(Endpoint source) {
        //TODO 目前EndPoint的数据都是从entry span获取的，可能不太对
        this.doEndPointAggregation(source);
    }

    private void doEndPointAggregation(Endpoint source) {
        CustomEndpointIndicator indicator = new CustomEndpointIndicator();
        indicator.setEntityId(source.getEntityId());
        indicator.setServiceId(source.getServiceId());
        indicator.setServiceInstanceId(source.getServiceInstanceId());
        indicator.setTimeBucket(source.getTimeBucket());
        indicator.combine(1, source.getLatency(), source.isStatus(), indicator.getLatestErrorTimeBucket());
        IndicatorProcess.INSTANCE.in(indicator);
    }
}

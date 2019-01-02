package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMCPU;

/**
 * @program: apm
 * @description: CPU实例调用器
 * @author: LiyuanLiu
 * @create: 2019-01-02 10:27
 **/
public class CPUInstanceDispatcher implements SourceDispatcher<ServiceInstanceJVMCPU> {
    @Override
    public void dispatch(ServiceInstanceJVMCPU source) {
        CPUGaugeService.INSTANCE.metric(source);
    }
}

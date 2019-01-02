package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMMemory;

/**
 * @program: apm
 * @description: 内存实例调用器
 * @author: LiyuanLiu
 * @create: 2019-01-02 10:23
 **/
public class MemoryInstanceDispatcher implements SourceDispatcher<ServiceInstanceJVMMemory> {
    @Override
    public void dispatch(ServiceInstanceJVMMemory source) {
        if (source.isHeapStatus()) {
            HeapMemoryGaugeService.INSTANCE.metric(source);
        } else {
            NoHeapMemoryGaugeService.INSTANCE.metric(source);
        }

    }
}

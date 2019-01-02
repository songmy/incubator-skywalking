package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMMemory;

/**
 * @program: apm
 * @description: Heap实例服务
 * @author: LiyuanLiu
 * @create: 2019-01-02 16:35
 **/
public enum HeapMemoryGaugeService {
    INSTANCE;

    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_jvm_heap_gauge").help("堆内存使用情况曲线")
            .labelNames(HeapMemoryGaugeService.INSTANCE.getLabelsNames()).register();

    public void metric(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        GAUGE.labels(HeapMemoryGaugeService.INSTANCE.labels(serviceInstanceJVMMemory)).set(serviceInstanceJVMMemory.getUsed());
    }

    private String[] labels(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        return new String[]{
                serviceInstanceJVMMemory.getServiceInstanceId() + "",
                serviceInstanceJVMMemory.getServiceName(),
                serviceInstanceJVMMemory.getName()
        };
    }

    private String[] getLabelsNames() {
        return new String[]{
                "serviceInstanceId",
                "serviceName",
                "name"
        };
    }
}

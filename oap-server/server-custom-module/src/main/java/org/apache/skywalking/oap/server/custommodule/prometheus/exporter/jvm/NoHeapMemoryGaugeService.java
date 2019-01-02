package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMMemory;

/**
 * @program: apm
 * @description: 非堆内存实例服务
 * @author: LiyuanLiu
 * @create: 2019-01-02 18:46
 **/
public enum NoHeapMemoryGaugeService {
    INSTANCE;

    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_jvm_no_heap_gauge").help("非堆内存使用情况曲线")
            .labelNames(NoHeapMemoryGaugeService.INSTANCE.getLabelsNames()).register();

    public void metric(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        GAUGE.labels(NoHeapMemoryGaugeService.INSTANCE.labels(serviceInstanceJVMMemory)).set(serviceInstanceJVMMemory.getUsed());
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

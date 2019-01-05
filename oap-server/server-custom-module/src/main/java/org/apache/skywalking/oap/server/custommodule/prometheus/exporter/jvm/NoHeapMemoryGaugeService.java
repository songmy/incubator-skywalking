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
    INSTANCEVALUE, INSTANCEFREE;

    private static final Gauge GAUGEVALUE = Gauge.build().name("neunn_paas_sw_jvm_no_heap_value_gauge").help("非堆内存使用情况曲线")
            .labelNames(NoHeapMemoryGaugeService.INSTANCEVALUE.getLabelsNames()).register();

    public void metricValue(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        GAUGEVALUE.labels(NoHeapMemoryGaugeService.INSTANCEVALUE.labels(serviceInstanceJVMMemory)).set(serviceInstanceJVMMemory.getUsed());
    }

    private static final Gauge GAUGEFREE = Gauge.build().name("neunn_paas_sw_jvm_no_heap_free_gauge").help("非堆内存剩余情况曲线")
            .labelNames(NoHeapMemoryGaugeService.INSTANCEFREE.getLabelsNames()).register();

    public void metricFree(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        long free = serviceInstanceJVMMemory.getMax() - serviceInstanceJVMMemory.getUsed();
        GAUGEFREE.labels(NoHeapMemoryGaugeService.INSTANCEFREE.labels(serviceInstanceJVMMemory)).set(
                free < 0 ? 0 : free);
    }

    private String[] labels(ServiceInstanceJVMMemory serviceInstanceJVMMemory) {
        return JVMServiceUtil.labels(serviceInstanceJVMMemory.getEntityId());
    }

    private String[] getLabelsNames() {
        return JVMServiceUtil.labelsNames();
    }
}

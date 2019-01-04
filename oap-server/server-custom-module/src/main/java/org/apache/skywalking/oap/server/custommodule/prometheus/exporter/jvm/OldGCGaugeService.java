package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMGC;

/**
 * @program: apm
 * @description: oldGC实例服务
 * @author: LiyuanLiu
 * @create: 2019-01-02 18:53
 **/
public enum OldGCGaugeService {
    INSTANCE;

    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_jvm_old_gc_gauge").help("oldGC使用情况曲线")
            .labelNames(OldGCGaugeService.INSTANCE.getLabelsNames()).register();

    public void metric(ServiceInstanceJVMGC serviceInstanceJVMGC) {
        GAUGE.labels(OldGCGaugeService.INSTANCE.labels(serviceInstanceJVMGC)).set(serviceInstanceJVMGC.getCount());
    }

    private String[] labels(ServiceInstanceJVMGC serviceInstanceJVMGC) {
        return JVMServiceUtil.labels(serviceInstanceJVMGC.getEntityId());
    }

    private String[] getLabelsNames() {
        return JVMServiceUtil.labelsNames();
    }
}

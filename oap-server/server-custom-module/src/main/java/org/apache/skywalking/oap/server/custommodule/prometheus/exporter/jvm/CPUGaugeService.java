package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMCPU;
import org.apache.skywalking.oap.server.core.source.Source;

/**
 * @program: apm
 * @description: CPU实例服务
 * @author: LiyuanLiu
 * @create: 2019-01-02 10:37
 **/
public enum CPUGaugeService {
    INSTANCE;

    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_jvm_cpu_gauge").help("中央处理器使用情况曲线")
            .labelNames(CPUGaugeService.INSTANCE.getLabelsNames()).register();

    public void metric(ServiceInstanceJVMCPU serviceInstanceJVMCPU) {
        GAUGE.labels(CPUGaugeService.INSTANCE.labels(serviceInstanceJVMCPU)).set(serviceInstanceJVMCPU.getUsePercent());
    }

    private String[] labels(Source serviceInstanceJVMCPU) {
        return JVMServiceUtil.labels(serviceInstanceJVMCPU.getEntityId());
    }

    private String[] getLabelsNames() {
        return JVMServiceUtil.labelsNames();
    }
}

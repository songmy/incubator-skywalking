package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import org.apache.skywalking.oap.server.core.analysis.SourceDispatcher;
import org.apache.skywalking.oap.server.core.source.GCPhrase;
import org.apache.skywalking.oap.server.core.source.ServiceInstanceJVMGC;

/**
 * @program: apm
 * @description: GC实例调用器
 * @author: LiyuanLiu
 * @create: 2019-01-02 10:34
 **/
public class GCInstanceDispatcher implements SourceDispatcher<ServiceInstanceJVMGC> {
    @Override
    public void dispatch(ServiceInstanceJVMGC source) {
        if (source.getPhrase() == GCPhrase.NEW) {
            YoungGCGaugeService.INSTANCE.metric(source);
        } else if (source.getPhrase() == GCPhrase.OLD) {
            OldGCGaugeService.INSTANCE.metric(source);
        }
    }
}

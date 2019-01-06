package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric;

import io.prometheus.client.Gauge;
import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.query.AggregationQueryService;
import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.core.query.entity.Step;
import org.apache.skywalking.oap.server.core.query.entity.TopNEntity;
import org.apache.skywalking.oap.server.library.module.ModuleManager;
import org.apache.skywalking.oap.server.library.util.TimeBucketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author songmy
 */
public enum ServiceSlaMetric {
    INSTANCE;
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSlaMetric.class);
    private static final String[] LABELS = {"serviceId", "serviceName"};
    private static final Gauge GAUGE = Gauge.build().name("neunn_paas_sw_service_sla_gauge").help("服务sla统计")
            .labelNames(LABELS).register();
    //    private ModuleManager moduleManager;
    private AggregationQueryService queryService;

    ServiceSlaMetric() {
    }

    public void run(ModuleManager moduleManager) {
        //TODO 统计最近2天的，演示需要数据
        long startTimeBucket = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(LocalDateTime.now().minusDays(3));
        long endTimeBucket = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(LocalDateTime.now());
        List<TopNEntity> entities = null;
        try {
            entities = getQueryService(moduleManager).getServiceTopN("service_sla", Integer.MAX_VALUE, Step.MINUTE, startTimeBucket, endTimeBucket, Order.ASC);
        } catch (IOException e) {
            LOGGER.error("统计servicesla错误：" + e.getMessage(), e);
        }
        if (entities != null && entities.size() > 0) {
            entities.forEach(entitie -> GAUGE.labels(entitie.getId(), entitie.getName()).set(entitie.getValue()));
        }
    }

    public AggregationQueryService getQueryService(ModuleManager moduleManager) {
        if (queryService == null) {
            this.queryService = moduleManager.find(CoreModule.NAME).provider().getService(AggregationQueryService.class);
        }
        return queryService;
    }
}

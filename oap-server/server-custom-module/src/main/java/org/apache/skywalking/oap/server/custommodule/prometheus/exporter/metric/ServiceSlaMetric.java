package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.metric;

import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.query.AggregationQueryService;
import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.core.query.entity.Step;
import org.apache.skywalking.oap.server.core.query.entity.TopNEntity;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.ExportMetricCatalog;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.IExportMetric;
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
public class ServiceSlaMetric implements IExportMetric {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceSlaMetric.class);
    private static final String[] LABELS = {"serviceId", "serviceName"};

    private static final String METRIC_NAME = "service_sla_gauge";
    private ModuleManager moduleManager;
    private AggregationQueryService queryService;

    public ServiceSlaMetric(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
    }

    public AggregationQueryService getQueryService(ModuleManager moduleManager) {
        if (queryService == null) {
            this.queryService = moduleManager.find(CoreModule.NAME).provider().getService(AggregationQueryService.class);
        }
        return queryService;
    }

    @Override
    public void registryMetric(ExportMetricCatalog exportMetricCatalog) {
        exportMetricCatalog.registryGauge(METRIC_NAME, "服务sla统计", LABELS);
    }

    @Override
    public void metricCollect(ExportMetricCatalog exportMetricCatalog) {
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
            entities.forEach(entitie -> exportMetricCatalog.updateGauge(METRIC_NAME, entitie.getValue(), entitie.getId(), entitie.getName()));
        }
    }
}

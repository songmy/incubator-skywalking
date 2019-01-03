package org.apache.skywalking.oap.server.custommodule.apis.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * @author songmy
 */
@Getter
@Setter
public class CustomEndPointIndicatorEntity {
    private String id;
    private String entityId;
    private String endPointName;
    private int serviceId;
    private String serviceName;
    private int serviceInstanceId;
    private long total;
    private long error;
    private long callAvgLatency;
    private long callAvg;
    private long errorRate;
    private long latestErrorTimeBucket;
}

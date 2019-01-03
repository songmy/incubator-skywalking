package org.apache.skywalking.oap.server.custommodule.receiver.trace;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.core.Const;
import org.apache.skywalking.oap.server.core.analysis.indicator.Indicator;
import org.apache.skywalking.oap.server.core.analysis.indicator.annotation.IndicatorType;
import org.apache.skywalking.oap.server.core.remote.annotation.StreamData;
import org.apache.skywalking.oap.server.core.remote.grpc.proto.RemoteData;
import org.apache.skywalking.oap.server.core.source.Scope;
import org.apache.skywalking.oap.server.core.storage.StorageBuilder;
import org.apache.skywalking.oap.server.core.storage.annotation.Column;
import org.apache.skywalking.oap.server.core.storage.annotation.IDColumn;
import org.apache.skywalking.oap.server.core.storage.annotation.StorageEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author songmy
 */
@IndicatorType
@StreamData
@StorageEntity(name = CustomEndpointIndicator.INDEX_NAME, builder = CustomEndpointIndicator.Builder.class, source = Scope.Endpoint)
public class CustomEndpointIndicator extends CustomAggregationIndicator {

    public static final String INDEX_NAME = "custom_endpoint_indicator";

    @Setter
    @Getter
    @Column(columnName = "entity_id")
    @IDColumn
    //就是定义的endpoint ID
    private java.lang.String entityId;
    @Setter
    @Getter
    @Column(columnName = "service_id")
    private int serviceId;

    @Setter
    @Getter
    @Column(columnName = "service_instance_id")
    private int serviceInstanceId;


    @Override
    public String id() {
        String splitJointId = String.valueOf(getTimeBucket());
        splitJointId += Const.ID_SPLIT + entityId;
        return splitJointId;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + entityId.hashCode();
        result = 31 * result + (int) getTimeBucket();
        return result;
    }

    @Override
    public int remoteHashCode() {
        int result = 17;
        result = 31 * result + entityId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        CustomEndpointIndicator indicator = (CustomEndpointIndicator) obj;
        if (!entityId.equals(indicator.entityId))
            return false;

        //noinspection RedundantIfStatement
        if (getTimeBucket() != indicator.getTimeBucket())
            return false;

        return true;
    }

    @Override
    public RemoteData.Builder serialize() {
        RemoteData.Builder remoteBuilder = RemoteData.newBuilder();
        remoteBuilder.addDataStrings(getEntityId());

        remoteBuilder.addDataLongs(getTotal());
        remoteBuilder.addDataLongs(getTimeBucket());
        remoteBuilder.addDataLongs(getError());
        remoteBuilder.addDataLongs(getSummation());
        remoteBuilder.addDataLongs(getCallavgLatency());
        remoteBuilder.addDataLongs(getCallAvg());
        remoteBuilder.addDataLongs(getErrorRate());


        remoteBuilder.addDataIntegers(getServiceId());
        remoteBuilder.addDataIntegers(getServiceInstanceId());

        return remoteBuilder;
    }

    @Override
    public void deserialize(RemoteData remoteData) {
        setEntityId(remoteData.getDataStrings(0));

        setTotal(remoteData.getDataLongs(0));
        setTimeBucket(remoteData.getDataLongs(1));
        setError(remoteData.getDataLongs(2));
        setSummation(remoteData.getDataLongs(3));
        setCallavgLatency(remoteData.getDataLongs(4));
        setCallAvg(remoteData.getDataLongs(5));
        setErrorRate(remoteData.getDataLongs(6));

        setServiceId(remoteData.getDataIntegers(0));
        setServiceInstanceId(remoteData.getDataIntegers(1));
    }

    @Override
    public Indicator toHour() {
        CustomEndpointIndicator indicator = new CustomEndpointIndicator();
        indicator.setEntityId(this.getEntityId());
        indicator.setServiceId(this.getServiceId());
        indicator.setServiceInstanceId(this.getServiceInstanceId());
        indicator.setTotal(this.getTotal());
        indicator.setSummation(this.getSummation());
        indicator.setError(this.getError());
        indicator.setErrorRate(this.getErrorRate());
        indicator.setCallAvg(this.getCallAvg());
        indicator.setCallavgLatency(this.getCallavgLatency());
        indicator.setTimeBucket(toTimeBucketInHour());
        return indicator;
    }

    @Override
    public Indicator toDay() {
        CustomEndpointIndicator indicator = new CustomEndpointIndicator();
        indicator.setEntityId(this.getEntityId());
        indicator.setServiceId(this.getServiceId());
        indicator.setServiceInstanceId(this.getServiceInstanceId());
        indicator.setTotal(this.getTotal());
        indicator.setSummation(this.getSummation());
        indicator.setError(this.getError());
        indicator.setErrorRate(this.getErrorRate());
        indicator.setCallAvg(this.getCallAvg());
        indicator.setCallavgLatency(this.getCallavgLatency());
        indicator.setTimeBucket(toTimeBucketInDay());
        return indicator;
    }

    @Override
    public Indicator toMonth() {
        CustomEndpointIndicator indicator = new CustomEndpointIndicator();
        indicator.setEntityId(this.getEntityId());
        indicator.setServiceId(this.getServiceId());
        indicator.setServiceInstanceId(this.getServiceInstanceId());
        indicator.setTotal(this.getTotal());
        indicator.setSummation(this.getSummation());
        indicator.setError(this.getError());
        indicator.setErrorRate(this.getErrorRate());
        indicator.setCallAvg(this.getCallAvg());
        indicator.setCallavgLatency(this.getCallavgLatency());
        indicator.setTimeBucket(toTimeBucketInMonth());
        return indicator;
    }


    public static class Builder implements StorageBuilder<CustomEndpointIndicator> {

        @Override
        public Map<String, Object> data2Map(CustomEndpointIndicator storageData) {
            Map<String, Object> map = new HashMap<>();
            map.put("entity_id", storageData.getEntityId());
            map.put("service_id", storageData.getServiceId());
            map.put("service_instance_id", storageData.getServiceInstanceId());
            map.put("total", storageData.getTotal());
            map.put("time_bucket", storageData.getTimeBucket());
            map.put(ERROR, storageData.getError());
            map.put(SUMMATION, storageData.getSummation());
            map.put(CALL_AVG_LATENCY, storageData.getCallavgLatency());
            map.put(CALL_AVG, storageData.getCallAvg());
            map.put(ERROR_RATE, storageData.getErrorRate());
            return map;
        }

        @Override
        public CustomEndpointIndicator map2Data(Map<String, Object> dbMap) {
            CustomEndpointIndicator indicator = new CustomEndpointIndicator();
            indicator.setEntityId((String) dbMap.get("entity_id"));
            indicator.setServiceId(((Number) dbMap.get("service_id")).intValue());
            indicator.setServiceInstanceId(((Number) dbMap.get("service_instance_id")).intValue());
            indicator.setTotal(((Number) dbMap.get("total")).longValue());
            indicator.setTimeBucket(((Number) dbMap.get("time_bucket")).longValue());
            indicator.setError(((Number) dbMap.get(ERROR)).longValue());
            indicator.setSummation(((Number) dbMap.get(SUMMATION)).longValue());
            indicator.setCallavgLatency(((Number) dbMap.get(CALL_AVG_LATENCY)).longValue());
            indicator.setCallAvg(((Number) dbMap.get(CALL_AVG)).longValue());
            indicator.setErrorRate(((Number) dbMap.get(ERROR_RATE)).longValue());
            return indicator;
        }
    }
}

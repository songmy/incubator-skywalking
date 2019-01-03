package org.apache.skywalking.oap.server.custommodule.storage.mysql.query;

import org.apache.skywalking.oap.server.core.Const;
import org.apache.skywalking.oap.server.core.analysis.indicator.Indicator;
import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.custommodule.apis.entity.CustomEndPointIndicatorEntity;
import org.apache.skywalking.oap.server.custommodule.receiver.trace.CustomAggregationIndicator;
import org.apache.skywalking.oap.server.custommodule.receiver.trace.CustomEndpointIndicator;
import org.apache.skywalking.oap.server.custommodule.storage.mysql.query.entity.CustomEndPointBrief;
import org.apache.skywalking.oap.server.custommodule.storage.query.IQueryCustomModuleDao;
import org.apache.skywalking.oap.server.library.client.jdbc.hikaricp.JDBCHikariCPClient;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author songmy
 */
public class QueryCustomModuleDaoImpl extends AbstractJdbcCustomDao implements IQueryCustomModuleDao {

    public QueryCustomModuleDaoImpl(JDBCHikariCPClient client) {
        super(client);
    }

    @Override
    public CustomEndPointBrief queryAggregation(int serviceId, int endPointId, int limit, int from,
                                                long startTimeBucket, long endTimeBucket, OrderBy orderBy, Order order) throws IOException {
        StringBuilder sql = new StringBuilder();
        StringBuilder sqlResult = new StringBuilder();
        sqlResult.append(" select  service_id ,service_instance_id ,")
                .append(Indicator.ENTITY_ID)
                .append(" ,avg(" + CustomAggregationIndicator.CALL_AVG_LATENCY + ")  ").append(CustomAggregationIndicator.CALL_AVG_LATENCY)
                .append(" ,avg(" + CustomAggregationIndicator.CALL_AVG + ")  ").append(CustomAggregationIndicator.CALL_AVG)
                .append(" ,avg(" + CustomAggregationIndicator.ERROR_RATE + ")  ").append(CustomAggregationIndicator.ERROR_RATE)
                .append(" ,sum(" + CustomAggregationIndicator.TOTAL + ")  ").append(CustomAggregationIndicator.TOTAL)
                .append(" ,sum(" + CustomAggregationIndicator.ERROR + ")  ").append(CustomAggregationIndicator.ERROR)
                .append(" ,max(" + CustomAggregationIndicator.CALL_LATEST_ERROR_TIME_BUCKET + ")  ").append(CustomAggregationIndicator.CALL_LATEST_ERROR_TIME_BUCKET);

        sql.append(" from ").append(CustomEndpointIndicator.INDEX_NAME).append(" where 1=1 ");
        List<Object> conditions = new ArrayList<>(10);
        if (serviceId > Const.USER_SERVICE_ID) {
            sql.append(" and service_id = ? ");
            conditions.add(serviceId);
        }
        if (endPointId > Const.USER_ENDPOINT_ID) {
            sql.append(" and entity_id = ? ");
            conditions.add(endPointId);
        }
        this.setTimeRangeCondition(sql, conditions, startTimeBucket, endTimeBucket);
        sql.append(" group by ").append(Indicator.ENTITY_ID);

        CustomEndPointBrief brief = new CustomEndPointBrief();
        try (Connection connection = getConnection()) {
            try (ResultSet resultSet = getClient().executeQuery(connection, "select count(1) total from (select 1 " + sql.toString() + " ) as indicator ", conditions.toArray())) {
                while (resultSet.next()) {
                    brief.setTotal(resultSet.getInt("total"));
                }
            }

            this.setOrderByCondition(sql, orderBy);
            sql.append(order.equals(Order.ASC) ? " asc " : " desc ");

            buildLimit(sql, from, limit, conditions);

            try (ResultSet rs = getClient().executeQuery(connection, sqlResult.append(sql).toString(), conditions.toArray())) {
                while (rs.next()) {
                    CustomEndPointIndicatorEntity customEndpointIndicator = new CustomEndPointIndicatorEntity();
                    customEndpointIndicator.setServiceId(rs.getInt("service_id"));
                    customEndpointIndicator.setServiceInstanceId(rs.getInt("service_instance_id"));
                    customEndpointIndicator.setEntityId(rs.getString(Indicator.ENTITY_ID));
                    customEndpointIndicator.setCallAvgLatency(rs.getLong(CustomAggregationIndicator.CALL_AVG_LATENCY));
                    customEndpointIndicator.setCallAvg(rs.getLong(CustomAggregationIndicator.CALL_AVG));
                    customEndpointIndicator.setErrorRate(rs.getLong(CustomAggregationIndicator.ERROR_RATE));
                    customEndpointIndicator.setTotal(rs.getLong(CustomAggregationIndicator.TOTAL));
                    customEndpointIndicator.setError(rs.getLong(CustomAggregationIndicator.ERROR));
                    customEndpointIndicator.setLatestErrorTimeBucket(rs.getLong(CustomAggregationIndicator.CALL_LATEST_ERROR_TIME_BUCKET));
                    brief.getData().add(customEndpointIndicator);
                }
            }
        } catch (SQLException e) {
            throw new IOException(e);
        }
        return brief;
    }

    private void buildLimit(StringBuilder sql, int from, int limit, List<Object> conditions) {
        sql.append(" LIMIT ").append(" ? ").append(", ").append(" ? ");
        conditions.add(from);
        conditions.add(limit);
    }

    private void setOrderByCondition(StringBuilder sql, OrderBy orderBy) {
        sql.append(" order by ");
        switch (orderBy) {
            case CALL_LATENCY:
                sql.append(" call_avg_latency ");
                break;
            case CALLS_ERROR:
                sql.append(" error ");
                break;
            case ERROR_RATE:
                sql.append(" error_rate ");
                break;
            case CALLS_COUNTER:
                sql.append(" total ");
                break;
            case CALL_AVG:
                sql.append(" call_avg ");
                break;
        }
    }

    private void setTimeRangeCondition(StringBuilder sql, List<Object> conditions, long startTimestamp,
                                       long endTimestamp) {
        sql.append(" and ").append(Indicator.TIME_BUCKET).append(" >= ? and ").append(Indicator.TIME_BUCKET).append(" <= ?");
        conditions.add(startTimestamp);
        conditions.add(endTimestamp);
    }
}

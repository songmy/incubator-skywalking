package org.apache.skywalking.oap.server.custommodule.storage.query;

import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.core.storage.DAO;
import org.apache.skywalking.oap.server.custommodule.storage.mysql.query.entity.CustomEndPointBrief;

import java.io.IOException;

/**
 * @author songmy
 */
public interface IQueryCustomModuleDao extends DAO {
    CustomEndPointBrief queryAggregation(int serviceId, int endPointId,
                                         int limit, int from,
                                         long startTimeBucket, long endTimeBucket, OrderBy orderBy, Order order) throws IOException;


    enum OrderBy {
        ERROR_RATE, CALLS_COUNTER, CALLS_ERROR, CALL_LATENCY, CALL_AVG
    }
}

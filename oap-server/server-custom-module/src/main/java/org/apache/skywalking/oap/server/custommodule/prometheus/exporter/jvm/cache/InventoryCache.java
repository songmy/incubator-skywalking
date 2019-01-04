package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm.cache;

import org.apache.skywalking.oap.server.core.cache.EndpointInventoryCache;
import org.apache.skywalking.oap.server.core.cache.ServiceInventoryCache;

/**
 * @program: apm
 * @description: 实例缓存
 * @author: LiyuanLiu
 * @create: 2019-01-04 14:25
 **/
public class InventoryCache {
    public static ServiceInventoryCache SERVICE_INVENTORY_CACHE;
    public static EndpointInventoryCache ENDPOINT_INVENTORY_CACHE;

    public InventoryCache(ServiceInventoryCache serviceInventoryCache, EndpointInventoryCache endpointInventoryCache) {
        if (serviceInventoryCache != null) {
            this.SERVICE_INVENTORY_CACHE = serviceInventoryCache;
        }

        if (endpointInventoryCache != null) {
            this.ENDPOINT_INVENTORY_CACHE = endpointInventoryCache;
        }
    }
}

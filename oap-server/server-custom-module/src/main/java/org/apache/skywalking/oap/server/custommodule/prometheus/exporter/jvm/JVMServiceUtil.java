package org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm;

import org.apache.skywalking.oap.server.core.register.EndpointInventory;
import org.apache.skywalking.oap.server.core.register.ServiceInventory;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm.cache.InventoryCache;

/**
 * @program: apm
 * @description: 工具类
 * @author: LiyuanLiu
 * @create: 2019-01-04 16:04
 **/
public class JVMServiceUtil {
    public static String[] labels(String entityId) {
        EndpointInventory endpointInventory = InventoryCache.ENDPOINT_INVENTORY_CACHE.get(Integer.parseInt(entityId));
        int serviceID = endpointInventory.getServiceId();
        ServiceInventory serviceInventory = InventoryCache.SERVICE_INVENTORY_CACHE.get(serviceID);
        String serviceName = serviceInventory.getName();

        return new String[]{
                entityId + "",
                serviceID + "",
                serviceName
        };
    }

    public static String[] labelsNames() {
        return new String[]{
                "serviceInstanceId",
                "serviceId",
                "serviceName"
        };
    }
}

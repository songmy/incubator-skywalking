package org.apache.skywalking.oap.server.custommodule.apis;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.skywalking.apm.util.StringUtil;
import org.apache.skywalking.oap.server.core.Const;
import org.apache.skywalking.oap.server.core.CoreModule;
import org.apache.skywalking.oap.server.core.cache.EndpointInventoryCache;
import org.apache.skywalking.oap.server.core.cache.ServiceInventoryCache;
import org.apache.skywalking.oap.server.core.query.entity.Order;
import org.apache.skywalking.oap.server.custommodule.ServerCustomModule;
import org.apache.skywalking.oap.server.custommodule.prometheus.exporter.jvm.cache.InventoryCache;
import org.apache.skywalking.oap.server.custommodule.storage.mysql.query.entity.CustomEndPointBrief;
import org.apache.skywalking.oap.server.custommodule.storage.query.IQueryCustomModuleDao;
import org.apache.skywalking.oap.server.custommodule.storage.query.PageInfo;
import org.apache.skywalking.oap.server.library.module.ModuleManager;
import org.apache.skywalking.oap.server.library.server.jetty.ArgumentsParseException;
import org.apache.skywalking.oap.server.library.server.jetty.JettyJsonHandler;
import org.apache.skywalking.oap.server.library.util.TimeBucketUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * @author songmy
 */
public class CustomQueryApisJettyHandler extends JettyJsonHandler {
    private static final long serialVersionUID = -8492838911011468767L;
    private static final Logger logger = LoggerFactory.getLogger(CustomQueryApisJettyHandler.class);

    private static final String QUERY = "query";
    private static final String VARIABLES = "variables";
    private static final String DATA = "data";
    private static final String ERRORS = "errors";
    private static final String MESSAGE = "message";
    private static final String PATH = "/custom/apis";
    private final Gson gson = new Gson();

    private static final String QUERY_QUERYENDPOINTAGGREGATION = "queryEndPointAggregation";

    private ModuleManager moduleManager;
    //    private final ServiceInstanceInventoryCache serviceInstanceInventoryCache;
    private final ServiceInventoryCache serviceInventoryCache;
    private final EndpointInventoryCache endpointInventoryCache;

    public CustomQueryApisJettyHandler(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
//        this.serviceInstanceInventoryCache = moduleManager.find(CoreModule.NAME).provider().getService(ServiceInstanceInventoryCache.class);
        this.serviceInventoryCache = moduleManager.find(CoreModule.NAME).provider().getService(ServiceInventoryCache.class);
        this.endpointInventoryCache = moduleManager.find(CoreModule.NAME).provider().getService(EndpointInventoryCache.class);
        InventoryCache.ENDPOINT_INVENTORY_CACHE = endpointInventoryCache;
        InventoryCache.SERVICE_INVENTORY_CACHE = serviceInventoryCache;
    }

    @Override
    public String pathSpec() {
        return PATH;
    }

    @Override
    protected JsonElement doGet(HttpServletRequest req) {
        throw new UnsupportedOperationException("custom/apis only supports POST method");
    }

    @Override
    protected JsonElement doPost(HttpServletRequest req) throws ArgumentsParseException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
        String line;
        StringBuilder request = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            request.append(line);
        }
        JsonObject requestJson = gson.fromJson(request.toString(), JsonObject.class);
        String query = requestJson.get(QUERY).getAsString();
        try {
            if (QUERY_QUERYENDPOINTAGGREGATION.equals(query)) {
                Map<String, Object> variables = gson.fromJson(requestJson.get(VARIABLES),
                        new TypeToken<Map<String, Object>>() {
                        }.getType());
                return this.queryEndPointAggregation(variables);
            }
        } catch (final Throwable t) {
            logger.error(t.getMessage(), t);
            JsonObject jsonObject = new JsonObject();
            JsonArray errorArray = new JsonArray();
            JsonObject errorJson = new JsonObject();
            errorJson.addProperty(MESSAGE, t.getMessage());
            errorArray.add(errorJson);
            jsonObject.add(ERRORS, errorArray);
            return jsonObject;
        }
        return null;
    }


    private JsonElement queryEndPointAggregation(Map<String, Object> variables) throws IOException {
        IQueryCustomModuleDao queryCustomModuleDao = moduleManager.find(ServerCustomModule.NAME).provider().getService(IQueryCustomModuleDao.class);
        int serviceId = ((Number) variables.getOrDefault("serviceId", Const.USER_SERVICE_ID)).intValue();
        int endPointId = ((Number) variables.getOrDefault("endPointId", Const.USER_ENDPOINT_ID)).intValue();
        int pageNum = ((Number) variables.getOrDefault("pageNum", 1)).intValue();
        int pageSize = ((Number) variables.getOrDefault("pageSize", 10)).intValue();
        PageInfo pageInfo = new PageInfo(pageSize, pageNum);
        int startIndex = pageInfo.getStartIndex();
        String orderByStr = (String) variables.getOrDefault("orderBy", IQueryCustomModuleDao.OrderBy.CALLS_COUNTER.name());
        IQueryCustomModuleDao.OrderBy orderBy = IQueryCustomModuleDao.OrderBy.valueOf(orderByStr);
        String orderStr = (String) variables.getOrDefault("order", Order.DES.name());
        Order order = Order.valueOf(orderStr);
        String startTBStr = (String) variables.getOrDefault("startTB", Const.EMPTY_STRING);
        String endTBStr = (String) variables.getOrDefault("endTB", Const.EMPTY_STRING);
        LocalDateTime startLocalDateTime;
        LocalDateTime endLocalDateTime;
        if (StringUtil.isEmpty(startTBStr) | StringUtil.isEmpty(endTBStr)) {
            startLocalDateTime = LocalDateTime.now();
            endLocalDateTime = startLocalDateTime.minusMinutes(15);
        } else {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
            startLocalDateTime = LocalDateTime.parse(startTBStr, dateTimeFormatter);
            endLocalDateTime = LocalDateTime.parse(endTBStr, dateTimeFormatter);
        }
        long startTB = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(startLocalDateTime);
        long endTB = TimeBucketUtils.INSTANCE.getMinuteTimeBucket(endLocalDateTime);


        CustomEndPointBrief customEndPointBrief = queryCustomModuleDao.queryAggregation(serviceId, endPointId, pageSize, startIndex, startTB, endTB, orderBy, order);
        if (customEndPointBrief != null && customEndPointBrief.getData().size() > 0) {
            customEndPointBrief.getData().forEach(customEndPointIndicatorEntity -> {
                customEndPointIndicatorEntity.setEndPointName(endpointInventoryCache.get(Integer.parseInt(customEndPointIndicatorEntity.getEntityId())).getName());
                customEndPointIndicatorEntity.setServiceName(serviceInventoryCache.get(customEndPointIndicatorEntity.getServiceId()).getName());
            });
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(DATA, gson.fromJson(gson.toJson(customEndPointBrief), JsonObject.class));
        return jsonObject;
    }

}

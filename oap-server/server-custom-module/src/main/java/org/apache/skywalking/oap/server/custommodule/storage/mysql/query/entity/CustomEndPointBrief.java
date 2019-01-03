package org.apache.skywalking.oap.server.custommodule.storage.mysql.query.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.skywalking.oap.server.custommodule.apis.entity.CustomEndPointIndicatorEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author songmy
 */
@Getter
public class CustomEndPointBrief {
    @Setter
    private int total;
    private List<CustomEndPointIndicatorEntity> data;

    public CustomEndPointBrief() {
        data = new ArrayList<>();
    }
}

package com.olshevchenko.optaplanner.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@ToString
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonIgnoreProperties({ "id" })
public class MapPoint {

    private final long id;
    private final double latitude;
    private final double longitude;

    @ToString.Exclude
    private Map<MapPoint, RouteDistanceDuration> routeDistanceDurationMap;

    public MapPoint(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public RouteDistanceDuration getRouteDistanceDuration(MapPoint mapPoint) {
        return routeDistanceDurationMap.get(mapPoint);
    }

}

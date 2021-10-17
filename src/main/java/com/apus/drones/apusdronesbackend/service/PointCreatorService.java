package com.apus.drones.apusdronesbackend.service;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Service;

@Service
public class PointCreatorService {

    private static final Integer EARTH_SURFACE_SRID = 4326;
    private final GeometryFactory geometryFactory;

    public PointCreatorService() {
        geometryFactory = new GeometryFactory(new PrecisionModel(), EARTH_SURFACE_SRID);
    }

    public Point createPoint(double x, double y) {
        return geometryFactory.createPoint(new Coordinate(x, y));
    }
}

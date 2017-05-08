package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.MyLocationEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;

public interface IGeoEntityVisitor {
    void visit(PointEntity entity);

    void visit(ImageEntity entity);

    void visit(UserEntity entity);

    void visit(MyLocationEntity entity);

    void visit(SensorEntity entity);

    void visit(AlertEntity entity);

    void visit(PolygonEntity entity);

    void visit(PolylineEntity entity);
}

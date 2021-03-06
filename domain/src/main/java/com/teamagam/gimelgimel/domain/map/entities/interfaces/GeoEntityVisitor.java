package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertPolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.ImageEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;

public interface GeoEntityVisitor {
  void visit(PointEntity entity);

  void visit(ImageEntity entity);

  void visit(UserEntity entity);

  void visit(AlertPointEntity entity);

  void visit(AlertPolygonEntity entity);

  void visit(PolygonEntity entity);

  void visit(PolylineEntity entity);
}

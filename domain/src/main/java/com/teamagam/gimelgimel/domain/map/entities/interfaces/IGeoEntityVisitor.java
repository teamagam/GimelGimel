package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;

public interface IGeoEntityVisitor {
    void visit(PointEntity point);
}

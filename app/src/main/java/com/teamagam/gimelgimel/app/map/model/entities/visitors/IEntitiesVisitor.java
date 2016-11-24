package com.teamagam.gimelgimel.app.map.model.entities.visitors;

import com.teamagam.gimelgimel.app.map.model.entities.Point;
import com.teamagam.gimelgimel.app.map.model.entities.Polygon;
import com.teamagam.gimelgimel.app.map.model.entities.Polyline;

/**
 * Created by Bar on 06-Mar-16.
 */
public interface IEntitiesVisitor {
    void visit(Point point);

    void visit(Polyline polyline);

    void visit(Polygon polygon);
}

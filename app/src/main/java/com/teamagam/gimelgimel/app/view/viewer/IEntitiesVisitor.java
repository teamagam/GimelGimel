package com.teamagam.gimelgimel.app.view.viewer;

import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polygon;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Polyline;

/**
 * Created by Bar on 06-Mar-16.
 */
public interface IEntitiesVisitor {
    void visit(Point point);
    void visit(Polyline polyline);
    void visit(Polygon polygon);
}

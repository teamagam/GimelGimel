package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.IVisitableEntity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Created by Bar on 29-Feb-16.
 *
 * Defines functionality needed for a vector entity
 *
 */
public interface Entity extends IVisitableEntity {

    String getId();

    Geometry getGeometry();

    Symbol getSymbol();

    void updateGeometry(Geometry geo);
    void updateSymbol(Symbol symbol);

    void setOnEntityChangedListener(EntityChangedListener ecl);

    void removeOnEntityChangedListener();

    interface EntityChangedListener{
        void OnEntityChanged(Entity changedEntity);
    }
}

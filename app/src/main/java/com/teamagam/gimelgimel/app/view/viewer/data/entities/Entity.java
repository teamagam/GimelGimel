package com.teamagam.gimelgimel.app.view.viewer.data.entities;

import com.teamagam.gimelgimel.app.view.viewer.IVisitableEntity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.Geometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

/**
 * Defines functionality needed for a vector entity
 */
public interface Entity extends IVisitableEntity {

    String getId();

    Geometry getGeometry();

    Symbol getSymbol();

    void updateGeometry(Geometry geo);

    void updateSymbol(Symbol symbol);

    void removeOnEntityChangedListener();

    /**
     * An interface needed to be implemented to register as a listener
     * for entity changes events
     */
    interface EntityChangedListener {
        void onEntityChanged(Entity changedEntity);
    }

    abstract class Builder{
        abstract void setOnEntityChangedListener(EntityChangedListener ecl);
    }
}

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

    void setOnEntityChangedListener(EntityChangedListener ecl);

    void removeOnEntityChangedListener();

    void setOnClickListener(OnClickListener clickListener);

    void removeOnClickListener();

    void clicked();

    /**
     * An interface needed to be implemented to register as a listener
     * for entity changes events.
     */
    interface EntityChangedListener{
        void onEntityChanged(Entity changedEntity);
    }

    /**
     * An interface needed to be implemented to register as a listener
     * for entity clicked events. mostly used for detecting map clicks.
     */
    interface OnClickListener{
        void onEntityClick(Entity entity);
    }


}

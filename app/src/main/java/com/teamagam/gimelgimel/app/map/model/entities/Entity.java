package com.teamagam.gimelgimel.app.map.model.entities;

import com.teamagam.gimelgimel.app.map.model.entities.visitors.IVisitableEntity;
import com.teamagam.gimelgimel.app.map.model.geometries.GeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;

/**
 * Defines functionality needed for a vector entity
 */
public interface Entity extends IVisitableEntity {

    String getId();

    GeometryApp getGeometry();

    SymbolApp getSymbol();

    void updateGeometry(GeometryApp geo);

    void updateSymbol(SymbolApp symbol);

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

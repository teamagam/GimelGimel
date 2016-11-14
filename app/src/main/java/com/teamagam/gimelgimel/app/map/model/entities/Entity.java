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

    String getText();
}

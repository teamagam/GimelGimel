package com.teamagam.gimelgimel.domain.map.entities.interfaces;

import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.SensorSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

public interface ISymbolVisitor {
    void visit(PointSymbol symbol);

    void visit(ImageSymbol symbol);

    void visit(UserSymbol symbol);

    void visit(MyLocationSymbol symbol);

    void visit(SensorSymbol symbol);

    void visit(AlertPointSymbol symbol);

    void visit(AlertPolygonSymbol symbol);

    void visit(PolygonSymbol symbol);

    void visit(PolylineSymbol symbol);
}

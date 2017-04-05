package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.graphics.Color;

import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.SensorSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

import java.util.Arrays;


public class SelectionSymbolizerVisitor implements ISymbolVisitor {

    private static final int SELECTION_BG_COLOR = Color.BLUE;
    private static final int SELECTION_CIRCLE_SIZE_DP = 25;
    private static final int SELECTION_POLYLINE_WIDTH = 2;

    private Symbol mEsriSymbol;
    private Symbol mBaseSymbol;

    public SelectionSymbolizerVisitor(Symbol baseSymbol) {
        mBaseSymbol = baseSymbol;
    }

    public Symbol getEsriSymbol() {
        return mEsriSymbol;
    }

    @Override
    public void visit(PointSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(ImageSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(UserSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(MyLocationSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(SensorSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(AlertSymbol symbol) {
        mEsriSymbol = getPointSelectionSymbol(mBaseSymbol);
    }

    @Override
    public void visit(PolygonSymbol symbol) {
        ((FillSymbol) mBaseSymbol).setOutline(new SimpleLineSymbol(
                SELECTION_BG_COLOR,
                SELECTION_POLYLINE_WIDTH,
                SimpleLineSymbol.STYLE.DASH));
        mEsriSymbol = mBaseSymbol;
    }

    @Override
    public void visit(PolylineSymbol symbol) {
        SimpleLineSymbol simpleLineSymbol = (SimpleLineSymbol) mBaseSymbol;
        simpleLineSymbol.setStyle(SimpleLineSymbol.STYLE.DASH);
        simpleLineSymbol.setColor(SELECTION_BG_COLOR);
        simpleLineSymbol.setWidth(SELECTION_POLYLINE_WIDTH);

        mEsriSymbol = simpleLineSymbol;
    }

    private Symbol getPointSelectionSymbol(Symbol baseSymbol) {
        Symbol selectionSymbol = new SimpleMarkerSymbol(
                SELECTION_BG_COLOR,
                SELECTION_CIRCLE_SIZE_DP,
                SimpleMarkerSymbol.STYLE.CIRCLE);
        return new CompositeSymbol(Arrays.asList(selectionSymbol, baseSymbol));
    }
}

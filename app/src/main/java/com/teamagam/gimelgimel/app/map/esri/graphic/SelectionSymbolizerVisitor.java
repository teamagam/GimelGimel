package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.Symbol;
import com.teamagam.gimelgimel.R;
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
    private static final int SELECTION_POLYLINE_WIDTH = 2;
    private static final int MAX_ALPHA = 255;
    private static final double SELECTION_ALPHA_PERCENTAGE = 0.5;

    private Symbol mEsriSymbol;
    private Symbol mBaseSymbol;
    private Context mContext;

    public SelectionSymbolizerVisitor(Context context, Symbol baseSymbol) {
        mBaseSymbol = baseSymbol;
        mContext = context;
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
        Drawable d = ContextCompat.getDrawable(mContext, R.drawable.ic_blank_circle);
        DrawableCompat.setTint(d, SELECTION_BG_COLOR);
        d.setAlpha((int) (MAX_ALPHA * SELECTION_ALPHA_PERCENTAGE));
        Symbol selectionSymbol = new PictureMarkerSymbol(d);
        return new CompositeSymbol(Arrays.asList(selectionSymbol, baseSymbol));
    }
}

package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.esri.core.symbol.CompositeSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.MyLocationSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.SensorSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;

import java.util.Arrays;

class EsriSymbolCreationVisitor implements ISymbolVisitor {

    private static final int SYMBOL_TEXT_SIZE_DP = 15;
    private static final int USER_CIRCLE_SIZE_DP = 10;
    private static final int MY_LOCATION_SYMBOL_SIZE_DP = 15;
    private static final int ACTIVE_USER_COLOR = Color.GREEN;
    private static final int STALE_USER_COLOR = Color.RED;
    private static final int MY_LOCATION_COLOR = Color.BLUE;
    private static final int DEFAULT_TINT_COLOR = Color.GREEN;
    private static final int POLYGON_OUTLINE_WIDTH = 2;
    private static final int POLYGON_COLOR = Color.DKGRAY;
    private static final int POLYGON_FILL_ALPHA_PERCENTAGE = 50;

    private final Context mContext;
    private Symbol mEsriSymbol;

    EsriSymbolCreationVisitor(Context context) {
        mEsriSymbol = null;
        mContext = context;
    }

    Symbol getEsriSymbol() {
        if (mEsriSymbol == null) {
            return getDefaultSymbol();
        }
        return mEsriSymbol;
    }

    @Override
    public void visit(PointSymbol symbol) {
        String pointType = symbol.getType();
        if ("building".equalsIgnoreCase(pointType)) {
            mEsriSymbol = createPictureMarker(R.drawable.ic_business, DEFAULT_TINT_COLOR);
        } else if ("enemy".equalsIgnoreCase(pointType)) {
            mEsriSymbol = createPictureMarker(R.drawable.ic_flare, DEFAULT_TINT_COLOR);
        } else {
            mEsriSymbol = createPictureMarker(R.drawable.ic_flag, DEFAULT_TINT_COLOR);
        }
    }

    @Override
    public void visit(ImageSymbol symbol) {
        mEsriSymbol = createPictureMarker(R.drawable.ic_camera, DEFAULT_TINT_COLOR);
    }

    @Override
    public void visit(UserSymbol symbol) {
        int symbolColor = symbol.isActive() ? ACTIVE_USER_COLOR : STALE_USER_COLOR;

        Symbol usernameSymbol = new TextSymbol(
                SYMBOL_TEXT_SIZE_DP,
                symbol.getUserName(),
                symbolColor,
                TextSymbol.HorizontalAlignment.CENTER,
                TextSymbol.VerticalAlignment.BOTTOM);

        Symbol simpleMarkerSymbol = new SimpleMarkerSymbol(
                symbolColor,
                USER_CIRCLE_SIZE_DP,
                SimpleMarkerSymbol.STYLE.CIRCLE);

        mEsriSymbol = new CompositeSymbol(Arrays.asList(usernameSymbol, simpleMarkerSymbol));
    }

    @Override
    public void visit(MyLocationSymbol symbol) {
        mEsriSymbol = new SimpleMarkerSymbol(
                MY_LOCATION_COLOR,
                MY_LOCATION_SYMBOL_SIZE_DP,
                SimpleMarkerSymbol.STYLE.CROSS);
    }

    @Override
    public void visit(SensorSymbol symbol) {
        //nothing for now, as they're not integrated
    }

    @Override
    public void visit(AlertSymbol symbol) {
        mEsriSymbol = createPictureMarker(R.drawable.ic_alert, DEFAULT_TINT_COLOR);
    }

    @Override
    public void visit(PolygonSymbol symbol) {
        SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(DEFAULT_TINT_COLOR);
        simpleFillSymbol.setAlpha(POLYGON_FILL_ALPHA_PERCENTAGE);
        simpleFillSymbol.setOutline(new SimpleLineSymbol(POLYGON_COLOR, POLYGON_OUTLINE_WIDTH));
        mEsriSymbol = simpleFillSymbol;
    }

    private Symbol getDefaultSymbol() {
        return new TextSymbol(10, "Pin", Color.RED);
    }

    private PictureMarkerSymbol createPictureMarker(int drawableId, int tintColor) {
        Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
        DrawableCompat.setTint(drawable, tintColor);
        return new PictureMarkerSymbol(drawable);
    }
}

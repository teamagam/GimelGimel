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
import com.teamagam.gimelgimel.app.common.utils.DisplayUtils;
import com.teamagam.gimelgimel.app.icons.IconProvider;
import com.teamagam.gimelgimel.domain.map.entities.interfaces.ISymbolVisitor;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.AlertPolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.ImageSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import java.util.Arrays;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class EsriSymbolCreationVisitor implements ISymbolVisitor {

  private static final int SYMBOL_TEXT_SIZE_DP = 15;
  private static final int DEFAULT_MARKER_SIZE = 10;
  private static final int DEFAULT_OUTLINE_WIDTH = 3;
  private static final int POLYGON_FILL_ALPHA_PERCENTAGE = 50;
  private static final int POINT_SYMBOL_ICON_DIMENSION_DP = 12;
  private static final SimpleLineSymbol.STYLE DEFAULT_OUTLINE_STYLE = SimpleLineSymbol.STYLE.SOLID;
  private final int mStaleUserColor;
  private final int mAlertTintColor;
  private final int mActiveUserColor;
  private final int mDefaultTintColor;
  private final int mDefaultBorderColor;
  private final Context mContext;
  private final OutlineStyleParser mOutlineStyleParser;
  private final IconProvider mIconProvider;
  private Symbol mEsriSymbol;

  @Inject
  EsriSymbolCreationVisitor(Context context, IconProvider iconProvider) {
    mEsriSymbol = null;
    mContext = context;
    mIconProvider = iconProvider;
    mOutlineStyleParser = new OutlineStyleParser();
    mDefaultTintColor = context.getColor(R.color.default_tint_color);
    mDefaultBorderColor = context.getColor(R.color.default_border_color);
    mAlertTintColor = context.getColor(R.color.alert_fill_color);
    mActiveUserColor = context.getColor(R.color.active_user_color);
    mStaleUserColor = context.getColor(R.color.alert_fill_color);
  }

  public Symbol getEsriSymbol() {
    if (mEsriSymbol == null) {
      return getDefaultSymbol();
    }
    return mEsriSymbol;
  }

  @Override
  public void visit(PointSymbol symbol) {
    int dimensionPx = DisplayUtils.dpToPx(POINT_SYMBOL_ICON_DIMENSION_DP);
    Drawable icon = mIconProvider.getIconDrawable(symbol.getIconId(), dimensionPx, dimensionPx);
    mEsriSymbol = new PictureMarkerSymbol(icon);
  }

  @Override
  public void visit(ImageSymbol symbol) {
    mEsriSymbol = createPictureMarker(R.drawable.ic_camera, mDefaultTintColor);
  }

  @Override
  public void visit(UserSymbol symbol) {
    int symbolColor = symbol.isActive() ? mActiveUserColor : mStaleUserColor;

    Symbol usernameSymbol = new TextSymbol(SYMBOL_TEXT_SIZE_DP, symbol.getUserName(), symbolColor,
        TextSymbol.HorizontalAlignment.CENTER, TextSymbol.VerticalAlignment.BOTTOM);

    Symbol simpleMarkerSymbol =
        new SimpleMarkerSymbol(symbolColor, DEFAULT_MARKER_SIZE, SimpleMarkerSymbol.STYLE.CIRCLE);

    mEsriSymbol = new CompositeSymbol(Arrays.asList(usernameSymbol, simpleMarkerSymbol));
  }

  @Override
  public void visit(AlertPointSymbol symbol) {
    mEsriSymbol = createPictureMarker(R.drawable.ic_alert, mDefaultTintColor);
  }

  @Override
  public void visit(AlertPolygonSymbol symbol) {
    mEsriSymbol = getFillSymbol(mAlertTintColor, mDefaultBorderColor, DEFAULT_OUTLINE_STYLE);
  }

  @Override
  public void visit(PolygonSymbol symbol) {
    int fillColor = Color.parseColor(symbol.getFillColor());
    int borderColor = getBorderColor(symbol);
    SimpleLineSymbol.STYLE style = getBorderStyle(symbol);
    mEsriSymbol = getFillSymbol(fillColor, borderColor, style);
  }

  @Override
  public void visit(PolylineSymbol symbol) {
    int borderColor = getBorderColor(symbol);
    SimpleLineSymbol.STYLE style = getBorderStyle(symbol);
    mEsriSymbol = new SimpleLineSymbol(borderColor, DEFAULT_OUTLINE_WIDTH, style);
  }

  private Symbol getDefaultSymbol() {
    return new TextSymbol(10, "Pin", Color.RED);
  }

  private PictureMarkerSymbol createPictureMarker(int drawableId, int tintColor) {
    Drawable drawable = ContextCompat.getDrawable(mContext, drawableId);
    DrawableCompat.setTint(drawable, tintColor);
    return new PictureMarkerSymbol(drawable);
  }

  private SimpleFillSymbol getFillSymbol(int fillColor,
      int outlineColor,
      SimpleLineSymbol.STYLE style) {
    SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(fillColor);
    simpleFillSymbol.setAlpha(POLYGON_FILL_ALPHA_PERCENTAGE);
    simpleFillSymbol.setOutline(new SimpleLineSymbol(outlineColor, DEFAULT_OUTLINE_WIDTH, style));
    return simpleFillSymbol;
  }

  private int getBorderColor(PolylineSymbol symbol) {
    return Color.parseColor(symbol.getBorderColor());
  }

  private SimpleLineSymbol.STYLE getBorderStyle(PolylineSymbol symbol) {
    return mOutlineStyleParser.parse(symbol.getBorderStyle());
  }

  private class OutlineStyleParser {

    private static final String STYLE_KEYWORD_SOLID = "solid";
    private static final String STYLE_KEYWORD_DASH = "dash";
    private static final String STYLE_KEYWORD_DOT = "dot";
    private static final String STYLE_KEYWORD_DASH_DOT = "dash-dot";
    private static final String STYLE_KEYWORD_DASH_DOT_DOT = "dash-dot-dot";

    SimpleLineSymbol.STYLE parse(String borderStyle) {
      if (STYLE_KEYWORD_SOLID.equalsIgnoreCase(borderStyle)) {
        return SimpleLineSymbol.STYLE.SOLID;
      }
      if (STYLE_KEYWORD_DASH.equalsIgnoreCase(borderStyle)) {
        return SimpleLineSymbol.STYLE.DASH;
      }
      if (STYLE_KEYWORD_DOT.equalsIgnoreCase(borderStyle)) {
        return SimpleLineSymbol.STYLE.DOT;
      }
      if (STYLE_KEYWORD_DASH_DOT.equalsIgnoreCase(borderStyle)) {
        return SimpleLineSymbol.STYLE.DASHDOT;
      }
      if (STYLE_KEYWORD_DASH_DOT_DOT.equalsIgnoreCase(borderStyle)) {
        return SimpleLineSymbol.STYLE.DASHDOTDOT;
      }
      return SimpleLineSymbol.STYLE.SOLID;
    }
  }
}

package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.Symbol;
import com.esri.arcgisruntime.symbology.TextSymbol;
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
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class EsriSymbolCreationVisitor implements ISymbolVisitor {

  private static final int DEFAULT_OUTLINE_WIDTH = 3;
  private static final int POLYGON_FILL_ALPHA_PERCENTAGE = 50;
  private static final int POINT_SYMBOL_ICON_DIMENSION_DP = 12;
  private static final SimpleLineSymbol.STYLE DEFAULT_OUTLINE_STYLE = SimpleLineSymbol.STYLE.SOLID;
  private final int mAlertTintColor;
  private final int mStaleUserColor;
  private final int mActiveUserColor;
  private final int mDefaultTintColor;
  private final int mDefaultBorderColor;
  private final Context mContext;
  private final OutlineStyleParser mOutlineStyleParser;
  private final IconProvider mIconProvider;
  private Symbol mEsriSymbol;
  private int mUserBackgroundColor;

  @Inject
  EsriSymbolCreationVisitor(Context context, IconProvider iconProvider) {
    mEsriSymbol = null;
    mContext = context;
    mIconProvider = iconProvider;
    mOutlineStyleParser = new OutlineStyleParser();
    mDefaultTintColor = ContextCompat.getColor(context, R.color.default_tint_color);
    mDefaultBorderColor = ContextCompat.getColor(context, R.color.default_border_color);
    mAlertTintColor = ContextCompat.getColor(context, R.color.alert_fill_color);
    mActiveUserColor = ContextCompat.getColor(context, R.color.active_user_color);
    mStaleUserColor = ContextCompat.getColor(context, R.color.stale_user_color);
    mUserBackgroundColor = ContextCompat.getColor(mContext, R.color.user_background_color);
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
    mEsriSymbol = PictureMarkerSymbol.createAsync(icon);
  }

  @Override
  public void visit(ImageSymbol symbol) {
    mEsriSymbol = createPictureMarker(R.drawable.ic_camera, mDefaultTintColor);
  }

  @Override
  public void visit(UserSymbol symbol) {
    int symbolColor = symbol.isActive() ? mActiveUserColor : mStaleUserColor;
    String text = symbol.getUserName();

    Bitmap bitmap = textAsBitmap(text, DisplayUtils.spToPx(6), symbolColor, mUserBackgroundColor);

    Drawable drawable = new BitmapDrawable(mContext.getResources(), bitmap);

    mEsriSymbol = new PictureMarkerSymbol(drawable);
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

  private Bitmap textAsBitmap(String text, float textSize, int textColor, int backgroundColor) {
    Paint paint = new Paint(Paint.SUBPIXEL_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
    paint.setTextSize(textSize);
    paint.setColor(textColor);
    paint.setTextAlign(Paint.Align.LEFT);
    float baseline = -paint.ascent(); // ascent() is negative
    int width = (int) (paint.measureText(text) + 0.5f); // round
    int height = (int) (baseline + paint.descent() + 0.5f);
    Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(image);
    canvas.drawColor(backgroundColor);
    canvas.drawText(text, 0, baseline, paint);
    return image;
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

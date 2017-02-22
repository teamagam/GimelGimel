/* Copyright 2012 ESRI
 *
 * All rights reserved under the copyright laws of the United States
 * and applicable international laws, treaties, and conventions.
 *
 * You may freely redistribute and use this sample code, with or
 * without modification, provided you include the original copyright
 * notice and use restrictions.
 *
 * See the sample code usage restrictions document for further information.
 *
 */

package com.teamagam.gimelgimel.app.map.esri.plugins;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnZoomListener;
import com.teamagam.gimelgimel.R;

import java.text.DecimalFormat;

public class ScaleBar extends TextView implements OnPinchListener, OnZoomListener {

    private static final int NO_OFFSET = 0;

    private final DecimalFormat mDecimalFormatter = new DecimalFormat("#,###,###,###");
    private MapView mMapView;

    private ScaleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyle();
    }

    public ScaleBar(Context context, AttributeSet attrs, MapView mapView) {
        this(context, attrs);
        mMapView = mapView;
        if (mMapView != null) {
            updateScale();
        }
    }

    @Override
    public void prePointersUp(float arg0, float arg1, float arg2, float arg3, double arg4) {
    }

    @Override
    public void prePointersMove(float arg0, float arg1, float arg2, float arg3, double arg4) {
    }

    @Override
    public void prePointersDown(float arg0, float arg1, float arg2, float arg3, double arg4) {
    }

    @Override
    public void postPointersUp(float arg0, float arg1, float arg2, float arg3, double arg4) {
    }

    @Override
    public void postPointersMove(float arg0, float arg1, float arg2, float arg3, double arg4) {
        updateScale();
    }

    @Override
    public void postPointersDown(float arg0, float arg1, float arg2, float arg3, double arg4) {
    }

    @Override
    public void preAction(float v, float v1, double v2) {
    }

    @Override
    public void postAction(float v, float v1, double v2) {
        updateScale();
    }

    private void setStyle() {
        setTextSize(getResources().getDimension(R.dimen.font_size_small));
        setTextColor(getResources().getColor(R.color.white));
        setShadowLayer(
                getResources().getDimension(R.dimen.small_shadow_radius), NO_OFFSET, NO_OFFSET,
                getResources().getColor(R.color.black));
        setBackgroundResource(R.drawable.rounded_corners);
    }

    private void updateScale() {
        setText(format(mMapView.getScale()));
    }

    private String format(double scale) {
        String formattedScale = mDecimalFormatter.format((int) scale);
        return String.format("1 : %s", String.valueOf(formattedScale));
    }
}
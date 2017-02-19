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

package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnZoomListener;

public class ScaleBar extends TextView implements OnPinchListener, OnZoomListener{

    private MapView mMapView;

    private ScaleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    private void updateScale() {
        setText(format(mMapView.getScale()));
    }

    private String format(double scale) {
        return String.format("1 : %s", String.valueOf((int) scale));
    }
}
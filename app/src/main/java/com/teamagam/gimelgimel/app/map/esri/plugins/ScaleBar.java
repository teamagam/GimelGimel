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
import com.teamagam.gimelgimel.R;

import java.text.DecimalFormat;

import rx.schedulers.Schedulers;

public class ScaleBar extends TextView implements SelfUpdatingViewPlugin {

    private static final int NO_OFFSET = 0;

    private final DecimalFormat mDecimalFormatter = new DecimalFormat("#,###,###,###");
    private MapView mMapView;
    private UIUpdatePoller mUIUpdatePoller;

    private ScaleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setStyle();
        mUIUpdatePoller = new ScaleBarUIUpdatePoller();
    }

    public ScaleBar(Context context, AttributeSet attrs, MapView mapView) {
        this(context, attrs);
        mMapView = mapView;
    }

    @Override
    public void start() {
        mUIUpdatePoller.start();
    }

    @Override
    public void stop() {
        mUIUpdatePoller.stop();
    }

    private void setStyle() {
        setTextSize(getResources().getDimension(R.dimen.font_size_small));
        setTextColor(getResources().getColor(R.color.white));
        setShadowLayer(
                getResources().getDimension(R.dimen.small_shadow_radius), NO_OFFSET, NO_OFFSET,
                getResources().getColor(R.color.black));
        setBackgroundResource(R.drawable.rounded_corners);
    }

    private String format(double scale) {
        String formattedScale = mDecimalFormatter.format((int) scale);
        return String.format("1 : %s", String.valueOf(formattedScale));
    }

    private class ScaleBarUIUpdatePoller extends UIUpdatePoller {

        ScaleBarUIUpdatePoller() {
            super(Schedulers.computation());
        }

        @Override
        protected void periodicalAction() {
            if (mMapView != null) {
                updateScale(mMapView.getScale());
            }
        }

        private void updateScale(double scale) {
            ScaleBar.this.post(() -> setText(format(scale)));
        }
    }
}
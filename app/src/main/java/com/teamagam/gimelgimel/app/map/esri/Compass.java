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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.esri.android.map.MapView;
import com.esri.android.map.event.OnPinchListener;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

import java.io.IOException;

public class Compass extends View {

    public static final String ASSET_NAME_NORTH_ARROW = "north.png";
    private static final AppLogger sLogger = AppLoggerFactory.create();
    private final Paint mPaint;
    private final Matrix mMatrix;

    private float mAngle;
    private Bitmap mBitmap;
    private MapView mMapView;

    private Compass(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mMatrix = new Matrix();
        mAngle = 0;
        mBitmap = getBitmap(context);
    }

    public Compass(Context context, AttributeSet attrs, MapView mapView) {
        this(context, attrs);
        mMapView = mapView;
        if (areViewsSet()) {
            mMapView.setOnPinchListener(new OnPinchListener() {

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
                    setRotationAngle(mMapView.getRotationAngle());
                }

                @Override
                public void postPointersDown(float arg0, float arg1, float arg2, float arg3, double arg4) {
                }
            });
        }
    }

    public void setRotationAngle(double angle) {
        mAngle = (float) angle;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        rotateMatrix();
        canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        super.onDraw(canvas);
    }

    private Bitmap getBitmap(Context context) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(ASSET_NAME_NORTH_ARROW));
        } catch (IOException e) {
            sLogger.e("Problem during compass loading: " + e);
            return null;
        }
    }

    private boolean areViewsSet() {
        return mMapView != null && mBitmap != null;
    }

    private void rotateMatrix() {
        mMatrix.reset();
        int centerX = mBitmap.getHeight() / 2;
        int centerY = mBitmap.getWidth() / 2;
        mMatrix.postRotate(-this.mAngle, centerX, centerY);
    }

}
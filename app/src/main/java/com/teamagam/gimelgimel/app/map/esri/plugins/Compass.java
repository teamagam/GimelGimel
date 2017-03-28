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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.esri.android.map.MapView;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

import java.io.IOException;

import rx.schedulers.Schedulers;

public class Compass extends View implements SelfUpdatingViewPlugin {

    public static final String ASSET_NAME_NORTH_ARROW = "north.png";
    private static final AppLogger sLogger = AppLoggerFactory.create();

    private final Paint mPaint;
    private final Matrix mMatrix;
    private final Bitmap mBitmap;
    private final MapView mMapView;

    private float mAngle;
    private int mHeight;
    private int mWidth;
    private int mCenterX;
    private int mCenterY;
    private UIUpdatePoller mCompassUIUpdatePoller;

    public Compass(Context context, AttributeSet attrs, MapView mapView) {
        super(context, attrs);
        mPaint = new Paint();
        mMatrix = new Matrix();
        mMapView = mapView;
        mBitmap = getBitmap(context);
        initPrimitives();
        if (mBitmap != null) {
            setDimens();
        }
        mCompassUIUpdatePoller = new CompassUIUpdatePoller();
    }

    @Override
    public void start() {
        if (areViewsSet()) {
            mCompassUIUpdatePoller.start();
        }
    }

    @Override
    public void stop() {
        mCompassUIUpdatePoller.stop();
    }

    public int getBitmapHeight() {
        return mHeight;
    }

    public int getBitmapWidth() {
        return mWidth;
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

    private void initPrimitives() {
        mAngle = 0;
        mHeight = 0;
        mWidth = 0;
    }

    private void setDimens() {
        mHeight = mBitmap.getHeight();
        mWidth = mBitmap.getWidth();
        mCenterX = mHeight / 2;
        mCenterY = mWidth / 2;
    }

    private boolean areViewsSet() {
        return mMapView != null && mBitmap != null;
    }

    private void rotateMatrix() {
        mMatrix.reset();
        mMatrix.postRotate(-this.mAngle, mCenterX, mCenterY);
    }

    private class CompassUIUpdatePoller extends UIUpdatePoller {

        CompassUIUpdatePoller() {
            super(Schedulers.computation());
        }

        @Override
        protected void periodicalAction() {
            setRotationAngle(mMapView.getRotationAngle());
        }

        private void setRotationAngle(double angle) {
            mAngle = (float) angle;
            Compass.this.postInvalidate();
        }
    }
}
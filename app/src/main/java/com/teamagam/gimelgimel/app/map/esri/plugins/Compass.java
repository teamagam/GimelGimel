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
import com.teamagam.gimelgimel.app.common.utils.Constants;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.plugins.RxJavaSchedulersHook;

public class Compass extends View {

    public static final String ASSET_NAME_NORTH_ARROW = "north.png";
    private static final AppLogger sLogger = AppLoggerFactory.create();
    private final Paint mPaint;
    private final Matrix mMatrix;

    private float mAngle;
    private Bitmap mBitmap;
    private MapView mMapView;
    private Subscription mRefreshSubscription;
    private boolean mIsRunning;

    public Compass(Context context, AttributeSet attrs, MapView mapView) {
        super(context, attrs);
        mPaint = new Paint();
        mMatrix = new Matrix();
        mAngle = 0;
        mBitmap = getBitmap(context);
        mIsRunning = false;
        mMapView = mapView;
    }

    public void start() {
        if (!mIsRunning && areViewsSet()) {
            mRefreshSubscription = Observable.interval(Constants.COMPASS_REFRESH_RATE_MS,
                    TimeUnit.MILLISECONDS)
                    .observeOn(RxJavaSchedulersHook.createComputationScheduler())
                    .map(x -> mMapView.getRotationAngle())
                    .doOnNext(this::setRotationAngle)
                    .subscribe();
            mIsRunning = true;
        }
    }

    public void stop() {
        if (mRefreshSubscription != null && !mRefreshSubscription.isUnsubscribed()) {
            mRefreshSubscription.unsubscribe();
        }
        mIsRunning = false;
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
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
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;

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
    private final Bitmap mBitmap;
    private final MapView mMapView;

    private float mAngle;
    private Subscription mRefreshSubscription;
    private boolean mIsRunning;
    private int mHeight;
    private int mWidth;
    private int mCenterX;
    private int mCenterY;

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
    }

    public void start() {
        if (!mIsRunning && areViewsSet()) {
            mRefreshSubscription = Observable.interval(Constants.COMPASS_REFRESH_RATE_MS,
                    TimeUnit.MILLISECONDS)
                    .observeOn(RxJavaSchedulersHook.createComputationScheduler())
                    .map(x -> mMapView.getRotationAngle())
                    .doOnNext(this::setRotationAngle)
                    .subscribe(new SimpleSubscriber<>());
            mIsRunning = true;
        }
    }

    public void stop() {
        if (mRefreshSubscription != null && !mRefreshSubscription.isUnsubscribed()) {
            mRefreshSubscription.unsubscribe();
        }
        mIsRunning = false;
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
        mIsRunning = false;
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

    private void setRotationAngle(double angle) {
        mAngle = (float) angle;
        postInvalidate();
    }
}
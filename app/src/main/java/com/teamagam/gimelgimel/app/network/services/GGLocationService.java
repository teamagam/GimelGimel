package com.teamagam.gimelgimel.app.network.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.injectors.components.DaggerServiceComponent;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.location.GetLocationStreamInteractor;
import com.teamagam.gimelgimel.domain.location.GetLocationStreamInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SendUserLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import javax.inject.Inject;

public class GGLocationService extends Service {

    private Looper mServiceLooper;
    private GGLocationServiceHandler mServiceHandler;

    @Inject
    GetLocationStreamInteractorFactory getLocationStreamInteractorFactory;
    @Inject
    SendUserLocationInteractorFactory sendUserLocationInteractorFactory;

    @Override
    public void onCreate() {
        DaggerServiceComponent.builder()
                .applicationComponent(((GGApplication) getApplication()).getApplicationComponent())
                .build()
                .inject(this);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("GGLocationServiceThread",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new GGLocationServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quitSafely();
    }

    // Handler that receives messages from the thread
    private final class GGLocationServiceHandler extends Handler {

        private GetLocationStreamInteractor mGetLocationStreamInteractor;

        public GGLocationServiceHandler(Looper looper) {
            super(looper);

            mGetLocationStreamInteractor = GGLocationService.this.getLocationStreamInteractorFactory.create(new GGLocationServiceSubscriber());

            mGetLocationStreamInteractor.execute();
        }
    }

    private final class GGLocationServiceSubscriber extends SimpleSubscriber<LocationSample> {
        @Override
        public void onNext(LocationSample locationSample) {
            GGLocationService.this
                    .sendUserLocationInteractorFactory.create(locationSample)
                    .execute();
        }
    }
}

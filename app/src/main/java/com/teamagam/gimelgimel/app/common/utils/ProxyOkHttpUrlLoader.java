package com.teamagam.gimelgimel.app.common.utils;


import android.content.Context;

import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.data.common.OkHttpClientFactory;

import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ProxyOkHttpUrlLoader extends OkHttpUrlLoader {
    private static AppLogger sLogger = AppLoggerFactory.create(ProxyOkHttpUrlLoader.class);

    public ProxyOkHttpUrlLoader(OkHttpClient client) {
        super(client);
    }

    public static class Factory implements ModelLoaderFactory<GlideUrl, InputStream> {
        private static volatile OkHttpClient sInternalClient;

        private OkHttpClient mClient;

        public Factory() {
            this(getInternalClient());
        }

        public Factory(OkHttpClient internalClient) {
            mClient = internalClient;
        }

        private static OkHttpClient getInternalClient() {
            if (sInternalClient == null) {
                synchronized (Factory.class) {
                    if (sInternalClient == null) {
                        sInternalClient = OkHttpClientFactory.create(sLogger,
                                HttpLoggingInterceptor.Level.HEADERS);
                    }
                }
            }

            return sInternalClient;
        }

        @Override
        public ModelLoader<GlideUrl, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new ProxyOkHttpUrlLoader(mClient);
        }

        @Override
        public void teardown() {
        }
    }
}

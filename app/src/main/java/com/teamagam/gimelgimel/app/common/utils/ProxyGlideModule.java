package com.teamagam.gimelgimel.app.common.utils;


import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;

import java.io.InputStream;

public class ProxyGlideModule implements GlideModule {
    private static AppLogger sLogger = AppLoggerFactory.create(GlideModule.class);

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        sLogger.e("applyOptions");
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        sLogger.e("registerComponents");
        glide.register(GlideUrl.class,
                InputStream.class,
                new ProxyOkHttpUrlLoader.Factory());
    }
}

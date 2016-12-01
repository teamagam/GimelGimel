package com.teamagam.gimelgimel.app.common.base.viewModel;

/**
 * Created by Gil on 18/08/2016.
 */
public interface ViewModel<V> {

    void setView(V v);

    void init();

    void start();

    void stop();

    void destroy();
}
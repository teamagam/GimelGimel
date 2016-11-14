package com.teamagam.gimelgimel.app.viewModels;


/**
 * Created by Gil on 18/08/2016.
 */
public interface ViewModel<V> {

    void setView(V v);

    void stop();

    void start();
}
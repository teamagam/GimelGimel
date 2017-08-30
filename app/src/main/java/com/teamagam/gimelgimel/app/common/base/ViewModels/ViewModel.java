package com.teamagam.gimelgimel.app.common.base.ViewModels;

public interface ViewModel<V> {

  void setView(V v);

  void init();

  void start();

  void stop();

  void destroy();
}
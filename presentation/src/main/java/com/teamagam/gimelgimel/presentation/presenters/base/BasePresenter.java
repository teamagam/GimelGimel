package com.teamagam.gimelgimel.presentation.presenters.base;

import rx.Subscriber;

public interface BasePresenter<T>{

    Subscriber<T> getNewSubscriber();

}

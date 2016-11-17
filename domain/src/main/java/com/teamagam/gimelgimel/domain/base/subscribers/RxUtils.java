package com.teamagam.gimelgimel.domain.base.subscribers;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created on 11/17/2016.
 */

public class RxUtils {

    public static <T> Observable<T> getReplayObservable(PublishSubject<T> subject, int numReplay){
        Observable<T> replayObservable = subject.share().replay(numReplay).autoConnect();
        replayObservable.subscribe();
        return replayObservable;
    }

    public static <T> Observable<T> getReplayObservable(PublishSubject<T> subject){
        Observable<T> replayObservable = subject.share().replay().autoConnect();
        replayObservable.subscribe();
        return replayObservable;
    }

}

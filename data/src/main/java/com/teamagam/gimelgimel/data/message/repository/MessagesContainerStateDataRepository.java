package com.teamagam.gimelgimel.data.message.repository;

import com.teamagam.gimelgimel.domain.messages.repository.MessagesContainerStateRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class MessagesContainerStateDataRepository implements MessagesContainerStateRepository {

    private ContainerState mState;

    @Inject
    public MessagesContainerStateDataRepository() {
        mState = ContainerState.VISIBLE;
    }

    @Override
    public Observable<ContainerState> getState() {
        return Observable.just(mState);
    }

    @Override
    public void toggleState() {
        mState = (mState == ContainerState.VISIBLE) ? ContainerState.INVISIBLE : ContainerState.VISIBLE;
    }
}

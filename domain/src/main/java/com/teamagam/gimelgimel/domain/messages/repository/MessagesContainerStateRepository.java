package com.teamagam.gimelgimel.domain.messages.repository;

import rx.Observable;

public interface MessagesContainerStateRepository {

    enum ContainerState{
        VISIBLE,
        INVISIBLE
    }

    Observable<ContainerState> getState();

    void toggleState();
}

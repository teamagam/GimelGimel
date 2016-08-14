package com.gimelgimel.domain.interactors.impl;

import com.gimelgimel.domain.executor.Executor;
import com.gimelgimel.domain.executor.MainThread;
import com.gimelgimel.domain.interactors.base.AbstractInteractor;

public class SendMessageInteractor extends AbstractInteractor {
    public SendMessageInteractor(Executor threadExecutor, MainThread mainThread) {
        super(threadExecutor, mainThread);
    }

    @Override
    public void run() {

    }
}

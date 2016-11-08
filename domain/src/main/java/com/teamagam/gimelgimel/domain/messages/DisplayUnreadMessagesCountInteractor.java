package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

@AutoFactory
public class DisplayUnreadMessagesCountInteractor extends DoInteractor<Integer> {

    private final Renderer mRenderer;
    private final MessagesRepository mMessagesRepository;

    protected DisplayUnreadMessagesCountInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided MessagesRepository messagesRepository,
            Renderer renderer) {
        super(threadExecutor);
        mMessagesRepository = messagesRepository;
        mRenderer = renderer;
    }

    @Override
    protected Observable<Integer> buildUseCaseObservable() {
        return Observable.just(mMessagesRepository)
                .flatMap(MessagesRepository::getNumUnreadMessagesObservable)
                .doOnNext(mRenderer::renderUnreadMessagesCount);
    }

    public interface Renderer{
        void renderUnreadMessagesCount(int unreadMessagesCount);
    }
}

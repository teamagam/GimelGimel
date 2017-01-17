package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

@AutoFactory
public class DisplayUnreadMessagesCountInteractor extends BaseSingleDisplayInteractor {

    private final Renderer mRenderer;
    private final MessagesRepository mMessagesRepository;

    protected DisplayUnreadMessagesCountInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            Renderer renderer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mRenderer = renderer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(mMessagesRepository.getNumUnreadMessagesObservable(), mRenderer::renderUnreadMessagesCount);
    }

    public interface Renderer{
        void renderUnreadMessagesCount(int unreadMessagesCount);
    }
}

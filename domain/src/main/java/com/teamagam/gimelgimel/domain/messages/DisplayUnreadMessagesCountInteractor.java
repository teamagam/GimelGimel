package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;

@AutoFactory
public class DisplayUnreadMessagesCountInteractor extends BaseSingleDisplayInteractor {

    private final Renderer mRenderer;
    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;

    protected DisplayUnreadMessagesCountInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
            Renderer renderer) {
        super(threadExecutor, postExecutionThread);
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
        mRenderer = renderer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
        return factory.create(mUnreadMessagesCountRepository.getNumUnreadMessagesObservable(), mRenderer::renderUnreadMessagesCount);
    }

    public interface Renderer{
        void renderUnreadMessagesCount(int unreadMessagesCount);
    }
}

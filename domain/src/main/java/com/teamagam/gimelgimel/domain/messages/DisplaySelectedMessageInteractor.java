package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Subscription;

@AutoFactory
public class DisplaySelectedMessageInteractor implements Interactor {


    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final Displayer mDisplayer;
    private MessagesRepository mMessagesRepository;
    private Subscription mSubscription;

    public DisplaySelectedMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            Displayer displayer) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mMessagesRepository = messagesRepository;
        mDisplayer = displayer;
    }

    @Override
    public void execute() {
        mSubscription = mMessagesRepository.getSelectedMessageObservable()
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        mDisplayer.display(message);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }

    public interface Displayer {
        void display(Message message);
    }
}

package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

@AutoFactory
public class DisplayMessagesInteractor implements Interactor {

    private final Displayer mDisplayer;
    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final MessagesRepository mMessagesRepository;

    private Subscription mMessagesSubscription;
    private Subscription mReadSubscription;
    private Subscription mSelectedSubscription;

    protected DisplayMessagesInteractor(
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
        mMessagesSubscription = execute(mMessagesRepository.getMessagesObservable(),
                new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        mDisplayer.show(message);
                    }
                });

        mReadSubscription = execute(mMessagesRepository.getReadMessagesObservable(),
                new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        mDisplayer.read(message);
                    }
                });

        mSelectedSubscription = execute(mMessagesRepository.getSelectedMessageObservable(),
                new SimpleSubscriber<Message>() {
                    @Override
                    public void onNext(Message message) {
                        mDisplayer.select(message);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        unsubscribe(mMessagesSubscription);
        unsubscribe(mReadSubscription);
        unsubscribe(mSelectedSubscription);
    }

    private <T> Subscription execute(Observable<T> observable, Subscriber<T> subscriber) {
        return observable
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(subscriber);
    }

    private void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public interface Displayer {
        void show(Message message);

        void read(Message message);

        void select(Message message);
    }
}

package com.teamagam.gimelgimel.app.model.ViewsModels;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * A singleton publisher-subscriber class for messages
 * <p/>
 * Use as middle-man between the message polling service and the
 * application UI thread.
 * <p/>
 * Notifies subscriber on all published messages
 */
public class MessagePubSub {

    public static final String LOG_TAG = MessagePubSub.class.getSimpleName();

    /**
     * Singleton pattern
     */
    private static MessagePubSub sInstance = new MessagePubSub();

    public static MessagePubSub getInstance() {
        return sInstance;
    }

    /**
     * Blocking collection used for consumer-producer pattern
     */
    private BlockingQueue<Message> mMessagesBlockingQueue;

    /**
     * Subscriber notifying thread
     */
    private Thread mSubscriberNotifyingThread;

    /**
     * The subscriber to notify on new message publish events
     */
    private NewMessagesSubscriber mSubscriber;

    private MessagePubSub() {
        mMessagesBlockingQueue = new LinkedBlockingQueue<>();
        mSubscriber = null;
        mSubscriberNotifyingThread = null;
    }

    /**
     * Start a new thread listening for new messages.
     * The thread notifies subscriber on publish events
     */
    public void subscribe(NewMessagesSubscriber subscriber) {
        if (subscriber == null) {
            throw new IllegalArgumentException("Given argument cannot be null!");
        }

        if (mSubscriber != null) {
            Log.e(LOG_TAG, "Cannot set a new subscriber since a subscriber is already registered!");
            return;
        }

        mSubscriber = subscriber;

        //Initiate consuming thread
        mSubscriberNotifyingThread = new SubscriberNotifyingThread(
                mMessagesBlockingQueue, mSubscriber);
        mSubscriberNotifyingThread.start();
    }

    /**
     * Stop notifying subscriber about new messages
     */
    public void unsubscribe() {
        if (mSubscriberNotifyingThread == null) {
            //No subscriber
            return;
        }

        Log.d(LOG_TAG, "Interrupting new message consuming thread");
        mSubscriberNotifyingThread.interrupt();
        mSubscriberNotifyingThread = null;
    }


    /**
     * Publish given message
     *
     * @param msg - message to publish
     */
    public void publish(Message msg) {
        if (msg == null) {
            Log.e(LOG_TAG, "Cannot publish null messages!");
            return;
        }

        mMessagesBlockingQueue.add(msg);

        Log.d(LOG_TAG, "New message added for publish. msg-id: " + msg.getMessageId());
    }


    public interface NewMessagesSubscriber {
        void onNewMessage(Message msg);
    }


    public static class SubscriberNotifyingThread extends Thread {
        private static final String LOG_TAG = SubscriberNotifyingThread.class.getSimpleName();

        private BlockingQueue<Message> mMessageBlockingQueue;
        private NewMessagesSubscriber mSubscriber;

        public SubscriberNotifyingThread(
                BlockingQueue<Message> messageBlockingQueue, NewMessagesSubscriber subscriber) {
            mMessageBlockingQueue = messageBlockingQueue;
            mSubscriber = subscriber;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    //Blocks run if BlockingQueue is empty
                    Message m = mMessageBlockingQueue.take();
                    mSubscriber.onNewMessage(m);
                }
            } catch (InterruptedException e) {
                //Fired when running thread is interrupted
                Log.d(LOG_TAG, "New messages notifier interrupted!");
            }
        }
    }
}


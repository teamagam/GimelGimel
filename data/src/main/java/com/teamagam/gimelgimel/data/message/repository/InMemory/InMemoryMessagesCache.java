package com.teamagam.gimelgimel.data.message.repository.InMemory;

import com.teamagam.gimelgimel.domain.messages.entity.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * class that holds its messages in-memory
 */
@Singleton
public class InMemoryMessagesCache {

    private final ArrayList<Message> mMessages;
    private Message mSelectedMessage;
    private Map mapEntitiesToMessage;

    @Inject
    InMemoryMessagesCache(){
        mMessages = new ArrayList<>();
    }

    public void addMessage(Message message){
        mMessages.add(message);
    }

    public void selectMessage(Message message) {
        if(mSelectedMessage != null) {
             mSelectedMessage.setSelected(false);
        }
        mSelectedMessage = message;
        if (mSelectedMessage != null) {
            mSelectedMessage.setSelected(true);
        }
    }

    public Message getMessageById(String id){
        for (Message msg:mMessages) {
            if(msg.getMessageId().equals(id)) {
                return msg;
            }
        }
        return null;
    }

    public Observable<Collection<Message>> getMessages() {
        return Observable.just(mMessages);
    }

    public void markMessageRead(Message message) {
        message.setRead(true);
    }

    public Message getSelectedMessage() {
        return mSelectedMessage;    }

}

package com.teamagam.gimelgimel.data.repository.InMemory;

import com.teamagam.gimelgimel.domain.messages.entities.Message;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * class that holds its messages in-memory
 */
@Singleton
public class InMemoryMessagesCache {

    private final ArrayList<Message> mMessage;

    @Inject
    InMemoryMessagesCache(){
        mMessage = new ArrayList<>();
    }

    public void addMessage(Message message){
        mMessage.add(message);
    }

}

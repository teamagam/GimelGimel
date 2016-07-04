package com.teamagam.gimelgimel.app.view.adapters;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.adapters.dummy.DummyMessagesContent;

/**
 * Created on 7/4/2016.
 * TODO: complete text
 */
public class MessageListViewModel {

    private OnDataChangedListener listener;

    public MessageListViewModel(OnDataChangedListener listener){
        this.listener = listener;
    }

    public DisplayMessagesRandomAccessor getRandomAccessor() {
        return new DisplayMessagesRandomAccessor() {
            @Override
            public int size() {
                return DummyMessagesContent.ITEMS.size();
            }

            @Override
            public Message get(int position) {
                return DummyMessagesContent.ITEMS.get(position);
            }
        };
    }

    public void addMessage(Message msg){
        DummyMessagesContent.ITEMS.add(msg);
        listener.onDataChanged();
    }

    public void setMessageSelected(Message msg) {
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.listener = listener;
    }

    /**
     * Created on 7/4/2016.
     * TODO: complete text
     */
    interface DisplayMessagesRandomAccessor extends IAdapterRandomAccessor<Message>{

    }

    /**
     * Created on 7/4/2016.
     * TODO: complete text
     */
    public interface OnDataChangedListener {
        void onDataChanged();
    }
}
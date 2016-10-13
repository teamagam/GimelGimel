package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.text.SimpleDateFormat;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Shared functionality of detail message view-model
 * Verifies selected message is of appropriate type before each method call.
 * Notifies observers on data changes only when selected message is of appropriate type.
 */
public abstract class MessageDetailViewModel<V> extends SelectedMessageViewModel<V>{

    public MessageDetailViewModel() {
        super();
    }

//    public void drawMessageOnMap(MessagesDetailBaseGeoFragment.GeoMessageInterface drawMessageOnMapInterface) {
//        drawMessageOnMapInterface.addMessageLocationPin(mSelectedMessageModel.getSelected());
//    }

    @Inject
    Context mContext;

    @Message.MessageType
    protected abstract String getExpectedMessageType();

    @Override
    protected boolean shouldNotifyOnSelectedMessage() {
        return isSelectedMessageOfType(getExpectedMessageType());
    }

    public String getTitleTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string
                .message_detail_title_time), Locale.ENGLISH);
        return sdf.format(getDate());
    }

    public String getTitleSender(){
        return getSenderId();
    }

    private boolean isSelectedMessageOfType(@Message.MessageType String messageType) {
        return mMessageSelected.getType().equals(messageType);
    }

}

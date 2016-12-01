package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.viewModel.BaseViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageApp;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Shared functionality of detail message view-model
 * Verifies selected message is of appropriate type before each method call.
 * Notifies observers on data changes only when selected message is of appropriate type.
 */
public abstract class MessageDetailViewModel<V> extends BaseViewModel<V> {

    private MessageApp mMessage;

    private Context mContext;

    public MessageDetailViewModel(Context context, MessageApp messageApp) {
        mMessage = messageApp;
        mContext = context;
    }

    public String getType() {
        return mMessage.getType();
    }

    public String getSenderId() {
        return mMessage.getSenderId();
    }

    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(mContext.getString(R.string
                .message_detail_title_time), Locale.ENGLISH);
        return sdf.format(mMessage.getCreatedAt());
    }
}

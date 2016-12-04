package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Shared functionality of detail message view-model
 * Verifies selected message is of appropriate type before each method call.
 * Notifies observers on data changes only when selected message is of appropriate type.
 */
public abstract class MessageDetailViewModel<V> extends BaseViewModel<V> {

    protected SimpleDateFormat mSimpleDateFormat;
    protected DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    protected DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;
    protected MessageApp mMessage;

    private Context mContext;

    public MessageDetailViewModel(Context context,
                                  DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory) {
        mContext = context;
        mDisplaySelectedMessageInteractorFactory = selectedMessageInteractorFactory;

        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.message_detail_title_time), Locale.ENGLISH);
    }

    @Override
    public void stop() {
        super.stop();

        if(mDisplaySelectedMessageInteractor != null) {
            mDisplaySelectedMessageInteractor.unsubscribe();
        }
    }

    @Override
    public void destroy() {
        super.destroy();

        if(mDisplaySelectedMessageInteractor != null) {
            mDisplaySelectedMessageInteractor.unsubscribe();
        }
    }

    public String getType() {
        if(mMessage != null) {
            return mMessage.getType();
        }

        return null;
    }

    public String getSenderId() {
        if(mMessage != null) {
            return mMessage.getSenderId();
        }

        return null;
    }

    public String getDate() {
        if(mMessage != null) {
            return mSimpleDateFormat.format(mMessage.getCreatedAt());
        }

        return null;
    }
}

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

    private Context mContext;
    private SimpleDateFormat mSimpleDateFormat;
    private DisplaySelectedMessageInteractorFactory mDisplaySelectedMessageInteractorFactory;
    private DisplaySelectedMessageInteractor mDisplaySelectedMessageInteractor;

    public MessageDetailViewModel(Context context,
                                   DisplaySelectedMessageInteractorFactory selectedMessageInteractorFactory) {
        mContext = context;
        mDisplaySelectedMessageInteractorFactory = selectedMessageInteractorFactory;

        mSimpleDateFormat = new SimpleDateFormat(mContext.getString(R.string.message_detail_title_time), Locale.ENGLISH);
    }

    @Override
    public void start() {
        super.start();

        DisplaySelectedMessageInteractor.Displayer displayer = createDisplayer();

        mDisplaySelectedMessageInteractor = mDisplaySelectedMessageInteractorFactory.create(displayer);
        mDisplaySelectedMessageInteractor.execute();
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
        MessageApp messageApp = getMessage();

        if(messageApp != null) {
            return messageApp.getType();
        }

        return null;
    }

    public String getSenderId() {
        MessageApp messageApp = getMessage();

        if(messageApp != null) {
            return messageApp.getSenderId();
        }

        return null;
    }

    public String getDate() {
        MessageApp messageApp = getMessage();

        if(messageApp != null) {
            return mSimpleDateFormat.format(messageApp.getCreatedAt());
        }

        return null;
    }

    protected abstract MessageApp getMessage();

    protected abstract DisplaySelectedMessageInteractor.Displayer createDisplayer();
}

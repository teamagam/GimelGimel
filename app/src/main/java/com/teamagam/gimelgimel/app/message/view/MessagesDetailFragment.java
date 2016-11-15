package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;

import com.teamagam.gimelgimel.app.message.model.MessageApp;
import com.teamagam.gimelgimel.app.message.viewModel.MessageDetailViewModel;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

/**
 * abstract class for detail message fragments.
 * It updates the title of the title view.
 */
public abstract class MessagesDetailFragment<VM extends MessageDetailViewModel> extends
        BaseDataFragment<VM> {

    private MessageApp mMessage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mMessage = ((MessagesContainerFragment)getTargetFragment()).getSelectedMessage();
    }

    protected MessageApp getMessage(){
        return mMessage;
    }
}

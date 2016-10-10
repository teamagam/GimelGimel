package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;


import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;

import butterknife.BindView;

/**
 * A Text {@link MessagesDetailFragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment{
//    TextMessageDetailViewModel

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;

    public MessagesDetailTextFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_text;
    }

    @Override
    protected BaseViewModel getSpecificViewModel() {
//        mViewModel = mApp.getTextMessageDetailViewModel();
        return null;
    }

    @Override
    protected void updateContentViews() {
//        mContentTV.setText(mViewModel.getText());
    }
}


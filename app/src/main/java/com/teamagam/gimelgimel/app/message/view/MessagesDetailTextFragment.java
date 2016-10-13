package com.teamagam.gimelgimel.app.message.view;


import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModel;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A Text {@link MessagesDetailFragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment<TextMessageDetailViewModel>{

    @Inject
    TextMessageDetailViewModel mViewModel;

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;

    public MessagesDetailTextFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_text;
    }

    @Override
    protected TextMessageDetailViewModel getSpecificViewModel() {
        return mViewModel;
    }

}


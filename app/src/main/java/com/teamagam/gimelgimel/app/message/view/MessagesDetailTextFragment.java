package com.teamagam.gimelgimel.app.message.view;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModelFactory;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.databinding.FragmentMessageTextBinding;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A Text {@link MessagesDetailFragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment<TextMessageDetailViewModel> {

    private static final String MESSAGE_KEY = MessagesDetailTextFragment.class.getSimpleName() + "message_key";

    @Inject
    TextMessageDetailViewModelFactory mViewModelFactory;

    private TextMessageDetailViewModel mViewModel;

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;

    public static MessagesDetailTextFragment create(MessageTextApp message) {
        MessagesDetailTextFragment fragment = new MessagesDetailTextFragment();

        Bundle args = new Bundle();
        args.putParcelable(MESSAGE_KEY, message);

        fragment.setArguments(args);

        return fragment;
    }

    public MessagesDetailTextFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);

        MessageTextApp message = getArguments().getParcelable(MESSAGE_KEY);

        mViewModel = mViewModelFactory.create(message);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_message_text;
    }

    @Override
    protected TextMessageDetailViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessageTextBinding bind = FragmentMessageTextBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        mViewModel.setView(this);
        return bind;
    }
}


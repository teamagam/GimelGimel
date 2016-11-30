package com.teamagam.gimelgimel.app.message.view;


import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.model.MessageTextApp;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModelFactory;
import com.teamagam.gimelgimel.app.mainActivity.MainActivity;
import com.teamagam.gimelgimel.databinding.FragmentMessageTextBinding;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * A Text {@link MessagesDetailFragment} subclass.
 */
public class MessagesDetailTextFragment extends MessagesDetailFragment<TextMessageDetailViewModel> {

    @Inject
    TextMessageDetailViewModelFactory mViewModelFactory;

    @BindView(R.id.fragment_text_message_content)
    TextView mContentTV;
    private TextMessageDetailViewModel mViewModel;

    public MessagesDetailTextFragment() {
        super();
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);

        mViewModel = mViewModelFactory.create((MessageTextApp) getMessage());
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


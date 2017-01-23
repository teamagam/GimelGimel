package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.view.fragments.RecyclerFragment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.app.message.viewModel.MessagesViewModel;
import com.teamagam.gimelgimel.databinding.FragmentMessagesMasterListBinding;

import javax.inject.Inject;

/**
 * A fragment representing a list of Messages.
 */
public class MessagesContainerFragment extends RecyclerFragment<MessagesViewModel> {

    @Inject
    MessagesViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected MessagesViewModel getSpecificViewModel() {
        return mViewModel;
    }

    @Override
    protected ViewDataBinding bindViewModel(View rootView) {
        FragmentMessagesMasterListBinding bind = FragmentMessagesMasterListBinding.bind(rootView);
        bind.setViewModel(mViewModel);
        mViewModel.setView(this);
        return bind;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    protected int getRecyclerId() {
        return R.id.fragment_messages_recycler;
    }
}

package com.teamagam.gimelgimel.app.message.view;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.message.viewModel.MessagesMasterViewModel;
import com.teamagam.gimelgimel.app.mainActivity.MainActivity;
import com.teamagam.gimelgimel.app.common.view.fragments.BaseDataFragment;
import com.teamagam.gimelgimel.databinding.FragmentMessagesMasterListBinding;

import javax.inject.Inject;

/**
 * A fragment representing a list of Messages.
 */
public class MessagesMasterFragment extends BaseDataFragment<MessagesMasterViewModel> {

    //    @BindView(R.id.fragment_messages_recycler)
    RecyclerView mRecyclerView;

    @Inject
    MessagesMasterViewModel mViewModel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    }

    @Override
    protected MessagesMasterViewModel getSpecificViewModel() {
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
    protected void createSpecificViews(View rootView) {
        super.createSpecificViews(rootView);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_messages_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mViewModel.getAdapter());
    }

}

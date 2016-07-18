package com.teamagam.gimelgimel.app.view.fragments.messags_panel_fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.DisplayMessage;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessagesViewModel;
import com.teamagam.gimelgimel.app.view.adapters.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.app.view.fragments.BaseDataFragment;

import butterknife.BindView;

/**
 * A fragment representing a list of Messages.
 */
public class MessagesMasterFragment extends BaseDataFragment<MessagesViewModel, GGApplication>
        implements MessagesRecyclerViewAdapter.OnItemClickListener {

    @BindView(R.id.fragment_messages_recycler)
    RecyclerView mRecyclerView;

    private MessagesRecyclerViewAdapter mAdapter;

    @Override
    protected void getSpecificViewModel() {
        mViewModel = mApp.getMessagesViewModel();
    }

    @Override
    protected void createSpecificViews(View rootView) {
        super.createSpecificViews(rootView);
        mAdapter = new MessagesRecyclerViewAdapter(
                mViewModel.getDisplayedMessagesRandomAccessor(), this);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.fragment_messages_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    public void onListItemInteraction(DisplayMessage message) {
        mViewModel.select(message);
    }

    @Override
    public void updateViewsOnUiThread() {
        mAdapter.notifyDataSetChanged();
    }

}

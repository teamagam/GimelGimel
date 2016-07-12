package com.teamagam.gimelgimel.app.view.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.DisplayMessage;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.MessagesViewModel;
import com.teamagam.gimelgimel.app.view.adapters.MessagesRecyclerViewAdapter;

import butterknife.BindView;

/**
 * A fragment representing a list of Items.
 */
public class MessagesMasterFragment extends BaseDataFragment<GGApplication>
        implements MessagesRecyclerViewAdapter.OnItemClickListener {


    @BindView(R.id.fragment_messages_recycler)
    RecyclerView mRecyclerView;

    private MessagesRecyclerViewAdapter mAdapter;
    private MessagesViewModel mMessagesViewModel;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        mMessagesViewModel = mApp.getMessagesViewModel();
        mMessagesViewModel.addObserver(this);
        mAdapter = new MessagesRecyclerViewAdapter(
                mMessagesViewModel.getDisplayedMessagesRandomAccessor(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    public void onListItemInteraction(DisplayMessage message) {
        mMessagesViewModel.select(message);
    }

    @Override
    public void onDataChanged() {
        notifyDataSetChangedOnUiThread();
    }

    private void notifyDataSetChangedOnUiThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}

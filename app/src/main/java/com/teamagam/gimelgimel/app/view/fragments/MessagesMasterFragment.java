package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.adapters.MessageListViewModel;
import com.teamagam.gimelgimel.app.view.adapters.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.app.view.adapters.dummy.DummyMessagesContent;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a list of Items.

 */
public class MessagesMasterFragment extends BaseFragment<GGApplication>
        implements MessagesRecyclerViewAdapter.OnItemClickListener, MessageListViewModel.OnDataChangedListener {

    //todo: needed for integration container MVVM
    //implemeents MessageListViewModel.OnDataChangedListener

    @BindView(R.id.fragment_messages_master_list)
    RecyclerView mRecyclerView;

    private MessagesRecyclerViewAdapter mAdapter;
    private MessageListViewModel mMessagesViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);

        ButterKnife.bind(this, rootView);

        mMessagesViewModel = new MessageListViewModel(this);
        mAdapter = new MessagesRecyclerViewAdapter(mMessagesViewModel.getRandomAccessor(), this);
        mRecyclerView.setAdapter(mAdapter);
        Context context = rootView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        return rootView;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    public void onListItemInteraction(Message item) {
        //todo: needed for integration container MVVM
        Toast.makeText(getActivity(), item.getMessageId(), Toast.LENGTH_SHORT).show();
        mMessagesViewModel.addMessage(DummyMessagesContent.createDummyItem(new Random().nextInt() % 100));

    }

    @Override
    public void onDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

}

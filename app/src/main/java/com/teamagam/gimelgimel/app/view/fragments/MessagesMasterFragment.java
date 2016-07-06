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
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMessageMasterFragmentClickListener}
 * interface.
 */
public class MessagesMasterFragment extends BaseFragment<GGApplication>
        implements MessagesRecyclerViewAdapter.OnItemClickListener, MessageListViewModel.OnDataChangedListener {
    //todo: needed for integration container MVVM
    //implemeents MessageListViewModel.OnDataChangedListener

    //todo: needed for integration container MVVM
    private OnMessageMasterFragmentClickListener mListener;

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
    public void onAttach(Context context) {
        super.onAttach(context);
        //todo: needed for integration container MVVM
//        if (context instanceof OnMessageMasterFragmentClickListener) {
//            mListener = (OnMessageMasterFragmentClickListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnItemClickListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //todo: needed for integration container MVVM
        mListener = null;
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

        //todo: needed for integration container MVVM
//        mListener.onListFragmentInteraction(item);
    }

    @Override
    public void onDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnMessageMasterFragmentClickListener {
        //todo: needed for integration container MVVM
        void onListFragmentInteraction(Message item);
    }


}

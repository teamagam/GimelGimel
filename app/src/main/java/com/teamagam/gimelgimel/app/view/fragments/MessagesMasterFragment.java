package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.view.fragments.dummy.DummyMessagesContent;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnMessageMasterFragmentClickListener}
 * interface.
 */
public class MessagesMasterFragment extends BaseFragment<GGApplication> implements MessageViewHolder.OnItemClickListener {

    private OnMessageMasterFragmentClickListener mListener;
    private RecyclerView mRecyclerView;
    private MessagesRecyclerViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages_master_list, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new MessagesRecyclerViewAdapter(DummyMessagesContent.ITEMS, this);
            mRecyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
        mListener = null;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_messages_master_list;
    }

    @Override
    public void onListItemInteraction(Message item) {
        Toast.makeText(getActivity(), item.getMessageId(), Toast.LENGTH_SHORT).show();
        DummyMessagesContent.addItem(DummyMessagesContent.createDummyItem(100));
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
        // TODO: Update argument type and name
        void onListFragmentInteraction(Message item);
    }


}

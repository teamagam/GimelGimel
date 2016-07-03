package com.teamagam.gimelgimel.app.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class MessagesMasterFragment extends BaseFragment<GGApplication> {

    private OnMessageMasterFragmentClickListener mListener;

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
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MessagesRecyclerViewAdapter(DummyMessagesContent.ITEMS, createCool()));
        }
        return view;
    }

    private cool createCool(){
        return new cool() {
            @Override
            public View newView(Message msg) {
                return null;
            }

            @Override
            public void bindView(View view, final Message msg) {
                ImageView mTypeView = (ImageView) view.findViewById(R.id.fragment_messages_master_icon);
                TextView mSenderView = (TextView) view.findViewById(R.id.fragment_messages_master_sender);
                TextView mTimeView = (TextView) view.findViewById(R.id.fragment_messages_master_time);
                mTypeView.setImageDrawable(view.getContext().getDrawable(android.R.drawable.ic_media_play));
                mSenderView.setText(msg.getSenderId());
                mTimeView.setText(msg.getCreatedAt().toString());

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mListener != null) {
                            // Notify the active callbacks interface (the activity, if the
                            // fragment is attached to one) that an item has been selected.
                            mListener .onListFragmentInteraction(msg);
                        }
                    }
                });
            }

            @Override
            public int countTypeView() {
                return 0;
            }

            @Override
            public int messageType(Message msg) {
                return 0;
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMessageMasterFragmentClickListener) {
            mListener = (OnMessageMasterFragmentClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMessageMasterFragmentClickListener");
        }
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

    public interface cool{
        View newView(Message msg);
        void bindView(View view, Message msg);
        int countTypeView();
        int messageType(Message msg);
    }
}

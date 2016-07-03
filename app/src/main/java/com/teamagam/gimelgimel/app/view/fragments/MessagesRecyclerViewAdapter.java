package com.teamagam.gimelgimel.app.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link MessagesMasterFragment.OnMessageMasterFragmentClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<cool> {

    private final List<Message> mValues;
    private final MessageViewAdapter.OnItemClickListener mListener;

    public MessagesRecyclerViewAdapter(List<Message> items, MessageViewAdapter.OnItemClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public cool onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(MessageViewAdapter.getLayout(), parent, false);
        return new MessageViewAdapter(view, mListener);
    }

    @Override
    public void onBindViewHolder(cool holder, int position) {
        Message message = mValues.get(position);
        holder.bindView(message);
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

}

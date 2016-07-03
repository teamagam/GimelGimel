package com.teamagam.gimelgimel.app.view.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Message} and makes a call to the
 * specified {@link MessagesMasterFragment.OnMessageMasterFragmentClickListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MessagesRecyclerViewAdapter extends RecyclerView.Adapter<MessagesRecyclerViewAdapter.ViewHolder> {

    private final List<Message> mValues;
    private final MessagesMasterFragment.cool mcool;

    public MessagesRecyclerViewAdapter(List<Message> items, MessagesMasterFragment.cool cool) {
        mValues = items;
        mcool = cool;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_messages_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        mcool.bindView(holder.mView, holder.mItem);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView mTypeView;
        public final TextView mTimeView;
        public final TextView mSenderView;
        public Message mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTypeView = (ImageView) view.findViewById(R.id.fragment_messages_master_icon);
            mSenderView = (TextView) view.findViewById(R.id.fragment_messages_master_sender);
            mTimeView = (TextView) view.findViewById(R.id.fragment_messages_master_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSenderView.getText() + "'";
        }
    }
}

package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * This boilerplate code helps manage the RecyclerView adapter code
 * It is designed to work when you:
 * <ul>
 * <li>Using ArrayList to manage your data</li>
 * <li>Have a single type of data in the adapter</li>
 * </ul>
 *
 * @param <VIEW_HOLDER> - The ViewHolder Type that references the views in a single item
 * @param <DATA>        - The Data Type that will be saved in the ArrayList
 */
public abstract class BaseRecyclerArrayAdapter<VIEW_HOLDER extends BaseRecyclerViewHolder<DATA>, DATA extends IdentifiedData>
        extends RecyclerView.Adapter<VIEW_HOLDER> {

    private final OnItemClickListener<DATA> mListener;
    private final SortedList<DATA> mSortedList;
    private final Map<String, DATA> mDataById;

    /**
     * Construct an adapter with data in it
     *
     * @param dataSortedList the data for the adapter to display
     */
    public BaseRecyclerArrayAdapter(SortedList<DATA> dataSortedList,
                                    OnItemClickListener<DATA> listener) {
        // Note this will be used internally by the adapter.
        // This is passed by reference, and by that is subject to changes from
        // outside the adapter.
        mSortedList = dataSortedList;
        mListener = listener;
        mDataById = new HashMap<>();
    }

    @Override
    public VIEW_HOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getListItemLayout(viewType),
                parent, false);
        return createNewViewHolder(view, viewType);
    }

    @Override
    public int getItemCount() {
        return mSortedList.size();
    }

    @Override
    public void onBindViewHolder(VIEW_HOLDER viewHolder, int position) {
        final DATA data = mSortedList.get(position);
        bindItemToView(viewHolder, data);
        bindOnClickListener(viewHolder, data);
    }

    public synchronized void show(DATA data) {
        if (isNewData(data)) {
            insertNewItem(data);
        } else {
            updateItem(data);
        }
    }

    public int getItemPosition(String messageId) {
        return mSortedList.indexOf(mDataById.get(messageId));
    }

    protected abstract VIEW_HOLDER createNewViewHolder(View v, int viewType);

    protected abstract void bindItemToView(VIEW_HOLDER holder, DATA data);

    protected abstract int getListItemLayout(int viewType);

    protected DATA get(int position) {
        return mSortedList.get(position);
    }

    protected DATA getById(String id) {
        return mDataById.get(id);
    }

    private boolean isNewData(DATA data) {
        return !mDataById.containsKey(data.getId());
    }

    private void insertNewItem(DATA data) {
        mSortedList.add(data);
        mDataById.put(data.getId(), data);
        notifyDataSetChanged();
    }

    private void updateItem(DATA updatedData) {
        DATA oldData = mDataById.get(updatedData.getId());
        mSortedList.updateItemAt(mSortedList.indexOf(oldData), updatedData);
        mDataById.put(updatedData.getId(), updatedData);
        notifyDataSetChanged();
    }

    private void bindOnClickListener(VIEW_HOLDER viewHolder, final DATA data) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onListItemInteraction(data);
            }
        });
    }

    public interface OnItemClickListener<DATA> {
        void onListItemInteraction(DATA item);
    }
}

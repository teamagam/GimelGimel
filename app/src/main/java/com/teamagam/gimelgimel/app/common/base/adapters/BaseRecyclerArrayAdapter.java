package com.teamagam.gimelgimel.app.common.base.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private final DataRandomAccessor<DATA> mAccessor;

    /**
     * Construct an adapter with data in it
     *
     * @param data the data for the adapter to display
     */
    public BaseRecyclerArrayAdapter(DataRandomAccessor<DATA> data,
                                    OnItemClickListener<DATA> listener) {
        // Note this will be used internally by the adapter.
        // This is passed by reference, and by that is subject to changes from
        // outside the adapter.
        mAccessor = data;
        mListener = listener;
    }

    @Override
    public VIEW_HOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getSingleItemLayoutRes(),
                parent, false);
        return createNewViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return mAccessor.size();
    }

    @Override
    public void onBindViewHolder(VIEW_HOLDER viewHolder, int position) {
        final DATA data = mAccessor.get(position);

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

    protected abstract VIEW_HOLDER createNewViewHolder(View v);

    protected abstract void bindItemToView(VIEW_HOLDER holder, DATA data);

    protected abstract int getSingleItemLayoutRes();

    private boolean isNewData(DATA data) {
        return mAccessor.getPosition(data.getId()) == -1;
    }

    private void updateItem(DATA data) {
        int idx = mAccessor.getPosition(data.getId());
        mAccessor.replace(idx, data);
        notifyItemChanged(idx);
    }


    private void insertNewItem(DATA data) {
        mAccessor.add(data);
        int newPosition = mAccessor.getPosition(data.getId());
        notifyItemInserted(newPosition);
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

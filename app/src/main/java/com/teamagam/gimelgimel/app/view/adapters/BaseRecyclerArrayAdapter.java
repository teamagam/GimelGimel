package com.teamagam.gimelgimel.app.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamagam.gimelgimel.app.common.DataRandomAccessor;

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
public abstract class BaseRecyclerArrayAdapter<VIEW_HOLDER extends BaseRecyclerViewHolder<DATA>, DATA>
        extends RecyclerView.Adapter<VIEW_HOLDER> {

    private final DataRandomAccessor<DATA> mAccessor;

    /**
     * Construct an adapter with data in it
     *
     * @param data the data for the adapter to display
     */
    public BaseRecyclerArrayAdapter(DataRandomAccessor<DATA> data) {
        // Note this will be used internally by the adapter.
        // This is passed by reference, and by that is subject to changes from
        // outside the adapter.
        mAccessor = data;
    }

    @Override
    public VIEW_HOLDER onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getSingleItemLayoutRes(), parent, false);
        return createNewViewHolder(view);
    }

    protected abstract VIEW_HOLDER createNewViewHolder(View v);

    @Override
    public void onBindViewHolder(VIEW_HOLDER viewHolder, int position) {
        bindItemToView(viewHolder,mAccessor.get(position) );
    }

    protected abstract void bindItemToView(VIEW_HOLDER holder, DATA data);

    @Override
    public int getItemCount() {
        return mAccessor.size();
    }

    protected abstract int getSingleItemLayoutRes();

}

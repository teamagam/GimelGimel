package com.teamagam.gimelgimel.app.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Represents an abstract view holder with auto-binding functions
 * NOTE: If subclassed from an Internal class, it must be a static class
 */
public abstract class BaseRecyclerViewHolder<DATA> extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public abstract View bindView(DATA data);

}

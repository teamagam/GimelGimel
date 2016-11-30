package com.teamagam.gimelgimel.app.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Represents an abstract view holder with auto-binding functions
 * NOTE: If subclassed from an Internal class, it must be a static class
 */
public abstract class BaseRecyclerViewHolder<DATA> extends RecyclerView.ViewHolder {

    public final Context mAppContext;

    public BaseRecyclerViewHolder(View itemView) {
        super(itemView);
        mAppContext = itemView.getContext().getApplicationContext();
        ButterKnife.bind(this, itemView);
    }

}

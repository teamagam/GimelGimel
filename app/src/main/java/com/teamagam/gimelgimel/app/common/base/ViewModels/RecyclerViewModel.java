package com.teamagam.gimelgimel.app.common.base.ViewModels;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.base.view.fragments.RecyclerFragment;

public abstract class RecyclerViewModel<T extends RecyclerFragment> extends BaseViewModel<T> {

    public abstract RecyclerView.Adapter getAdapter();
}

package com.teamagam.gimelgimel.app.common.base.ViewModels;

import android.support.v7.widget.RecyclerView;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.base.view.fragments.RecyclerFragment;

/**
 * TODO: add class summary notes
 */
public abstract class RecyclerViewModel extends BaseViewModel<RecyclerFragment> {

    public abstract RecyclerView.Adapter getAdapter();
}

package com.teamagam.gimelgimel.app.common.base.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.teamagam.gimelgimel.app.common.base.ViewModels.RecyclerViewModel;

public abstract class RecyclerFragment<T extends RecyclerViewModel> extends BaseDataFragment<T> {

    @Override
    protected void createSpecificViews(View rootView) {
        super.createSpecificViews(rootView);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(getRecyclerId());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(getSpecificViewModel().getAdapter());
    }

    protected abstract int getRecyclerId();
}

package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.content.Intent;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.mainActivity.view.ToolbarFragment;
import com.teamagam.gimelgimel.app.map.view.DrawActionActivity;

public class ToolbarViewModel extends BaseViewModel<ToolbarFragment> {

    public ToolbarViewModel() {
    }

    public void onSendPolygonClicked() {
        mView.startActivity(new Intent(mView.getContext(), DrawActionActivity.class));
    }

    public void onDrawGeometryClicked() {

    }

    public void onMeasureDistanceClicked() {

    }

    public void onGoToLocationClicked() {
        Navigator.openGoToDialog(mView.getActivity());

    }
}
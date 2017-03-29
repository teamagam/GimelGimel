package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.draw_action_fragment_container, getFragment())
                .commit();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_action_draw;
    }

    private BaseDrawActionFragment getFragment() {
        return new SendQuadrilateralActionFragment();
    }
}

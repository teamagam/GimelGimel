package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    private BaseDrawActionFragment mMainFragment;
    private DrawActionBottomPanelFragment mPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainFragment = getMainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.draw_action_fragment_container, mMainFragment)
                .commit();
        mPanelFragment = (DrawActionBottomPanelFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_bottom_panel);
        mPanelFragment.setPositiveText(mMainFragment.getPositiveButtonText());
        mPanelFragment.setOnPositiveClickListener(v -> mMainFragment.onPositiveButtonClick());
        mPanelFragment.setOnCancelClickListener(v -> finish());
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_action_draw;
    }

    private BaseDrawActionFragment getMainFragment() {
        return new ExampleDrawActionFragment();
    }
}

package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    private BaseDrawActionFragment mMainFragment;
    private DrawActionBottomPanelFragment mBottomPanelFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainFragment();
        initBottomPanelFragment();
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_action_draw;
    }

    private void initMainFragment() {
        mMainFragment = getMainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.draw_action_fragment_container, mMainFragment)
                .commit();
    }

    private BaseDrawActionFragment getMainFragment() {
        return new ExampleDrawActionFragment();
    }

    private void initBottomPanelFragment() {
        mBottomPanelFragment = (DrawActionBottomPanelFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_bottom_panel);
        getSupportFragmentManager().executePendingTransactions();
        mBottomPanelFragment.setPositiveText(mMainFragment.getPositiveButtonText());
        mBottomPanelFragment.setOnPositiveClickListener(v -> mMainFragment.onPositiveButtonClick());
        mBottomPanelFragment.setOnCancelClickListener(v -> finish());
    }
}

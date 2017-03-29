package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    private BaseDrawActionFragment mMainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initMainFragment();
        initActionFab();
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
        return new SendQuadrilateralActionFragment();
    }

    private void initActionFab() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.draw_action_fab);
        getSupportFragmentManager().executePendingTransactions();
        fab.setImageDrawable(mMainFragment.getFabActionDrawable());
        fab.setOnClickListener(v -> mMainFragment.onActionFabClick());
    }
}

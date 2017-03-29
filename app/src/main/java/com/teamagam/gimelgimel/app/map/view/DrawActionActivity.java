package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;

import butterknife.BindView;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    @BindView(R.id.action_toolbar)
    Toolbar mToolbar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseDrawActionFragment fragment = getFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.draw_action_fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(fragment.getToolbarTitle());
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_action_draw;
    }

    private BaseDrawActionFragment getFragment() {
        return new SendQuadrilateralActionFragment();
    }
}

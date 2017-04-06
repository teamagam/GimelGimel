package com.teamagam.gimelgimel.app.map.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

import butterknife.BindView;

public class DrawActionActivity extends BaseActivity<GGApplication> {

    private static final Logger sLogger = AppLoggerFactory.create();

    private static final String ACTION_TAG = "action";
    private static final String SEND_QUAD_ACTION = "send_quad";
    private static final String MEASURE_DISTANCE_ACTION = "measure";

    public static void startSendQuadAction(Context context) {
        startActionActivity(context, SEND_QUAD_ACTION);
    }

    public static void startMeasureAction(Context context) {
        startActionActivity(context, MEASURE_DISTANCE_ACTION);
    }


    private static void startActionActivity(Context context, String action) {
        Intent intent = new Intent(context, DrawActionActivity.class);
        intent.putExtra(ACTION_TAG, action);
        context.startActivity(intent);
    }


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
        String action = getIntent().getStringExtra(ACTION_TAG);
        if (isValidIntentAction(action)) {
            BaseDrawActionFragment fragment = setupActionFragment(action);
            setupActionBar(fragment);
        } else {
            sLogger.e("Unknown action requested (" + action + "). Closing action-activity");
            finish();
        }
    }

    @Override
    protected int getActivityLayout() {
        return R.layout.activity_action_draw;
    }

    private boolean isValidIntentAction(String action) {
        return SEND_QUAD_ACTION.equalsIgnoreCase(action) ||
                MEASURE_DISTANCE_ACTION.equalsIgnoreCase(action);
    }

    private BaseDrawActionFragment setupActionFragment(String action) {
        BaseDrawActionFragment fragment = getActionFragment(action);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.draw_action_fragment_container, fragment)
                .commit();
        getSupportFragmentManager().executePendingTransactions();
        return fragment;
    }

    private BaseDrawActionFragment getActionFragment(String action) {
        if (SEND_QUAD_ACTION.equalsIgnoreCase(action)) {
            return new SendQuadrilateralActionFragment();
        } else {
            return new MeasureActionFragment();
        }
    }

    private void setupActionBar(BaseDrawActionFragment fragment) {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(fragment.getToolbarTitle());
    }
}

package com.teamagam.gimelgimel.app.map.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.measure.MeasureActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.geometry.SendGeometryActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.quadrilateral.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

public class DrawActionActivity extends BaseActivity<GGApplication> {

  private static final Logger sLogger = AppLoggerFactory.create();

  private static final String ACTION_TAG = "action";
  private static final String SEND_QUAD_ACTION = "send_quad";
  private static final String MEASURE_DISTANCE_ACTION = "measure";
  private static final String SEND_GEOMETRY_ACTION = "send_geometry";
  private static final String EDIT_DYNAMIC_LAYER = "edit_dynamic_layer";
  private static final String FREE_DRAW_ACTION = "free_draw";

  @BindView(R.id.action_toolbar)
  Toolbar mToolbar;

  public static void startSendQuadAction(Context context) {
    startActionActivity(context, SEND_QUAD_ACTION);
  }

  public static void startMeasureAction(Context context) {
    startActionActivity(context, MEASURE_DISTANCE_ACTION);
  }

  public static void startSendGeometryAction(Context context) {
    startActionActivity(context, SEND_GEOMETRY_ACTION);
  }

  public static void startDynamicLayerEditAction(Context context) {
    startActionActivity(context, EDIT_DYNAMIC_LAYER);
  }

  public static void startFreeDrawAction(Context context) {
    startActionActivity(context, FREE_DRAW_ACTION);
  }

  private static void startActionActivity(Context context, String action) {
    Intent intent = new Intent(context, DrawActionActivity.class);
    intent.putExtra(ACTION_TAG, action);
    context.startActivity(intent);
  }

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
    return SEND_QUAD_ACTION.equalsIgnoreCase(action)
        || MEASURE_DISTANCE_ACTION.equalsIgnoreCase(action)
        || SEND_GEOMETRY_ACTION.equalsIgnoreCase(action)
        || EDIT_DYNAMIC_LAYER.equalsIgnoreCase(action)
        || FREE_DRAW_ACTION.equalsIgnoreCase(action);
  }

  private BaseDrawActionFragment setupActionFragment(String action) {
    BaseDrawActionFragment fragment = getActionFragment(action);
    getSupportFragmentManager().beginTransaction()
        .add(R.id.draw_action_fragment_container, fragment)
        .commit();
    getSupportFragmentManager().executePendingTransactions();
    return fragment;
  }

  private BaseDrawActionFragment getActionFragment(String action) {
    if (SEND_QUAD_ACTION.equalsIgnoreCase(action)) {
      return new SendQuadrilateralActionFragment();
    } else if (MEASURE_DISTANCE_ACTION.equalsIgnoreCase(action)) {
      return new MeasureActionFragment();
    } else if (SEND_GEOMETRY_ACTION.equalsIgnoreCase(action)) {
      return new SendGeometryActionFragment();
    } else if (EDIT_DYNAMIC_LAYER.equalsIgnoreCase(action)) {
      return new DynamicLayerActionFragment();
    } else if (FREE_DRAW_ACTION.equalsIgnoreCase(action)) {
      return new FreeDrawActionFragment();
    } else {
      throw new RuntimeException("Unsupported action - " + action);
    }
  }

  private void setupActionBar(BaseDrawActionFragment fragment) {
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    setTitle(fragment.getToolbarTitle());
  }
}

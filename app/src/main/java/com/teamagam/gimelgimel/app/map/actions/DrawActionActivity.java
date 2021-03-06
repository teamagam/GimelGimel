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
import com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers.EditDynamicLayerActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.geometry.SendGeometryActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.quadrilateral.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.app.map.actions.timeplay.TimeplayActionFragment;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public class DrawActionActivity extends BaseActivity<GGApplication> {

  private static final Logger sLogger = AppLoggerFactory.create();

  private static final String ACTION_TAG = "action";
  private static final String SEND_QUAD_ACTION = "send_quad";
  private static final String MEASURE_DISTANCE_ACTION = "measure";
  private static final String SEND_GEOMETRY_ACTION = "send_geometry";
  private static final String EDIT_DYNAMIC_LAYER = "edit_dynamic_layer";
  private static final String FREE_DRAW_ACTION = "free_draw";
  private static final String TIMEPLAY_ACTION = "timeplay";
  private static final String DYNAMIC_LAYER_ID_KEY = "dynamic_layer_id_key";

  @BindView(R.id.action_toolbar)
  Toolbar mToolbar;
  private BaseDrawActionFragment mFragment;

  public static void startSendQuadAction(Context context) {
    startActionActivity(context, SEND_QUAD_ACTION);
  }

  public static void startMeasureAction(Context context) {
    startActionActivity(context, MEASURE_DISTANCE_ACTION);
  }

  public static void startSendGeometryAction(Context context) {
    startActionActivity(context, SEND_GEOMETRY_ACTION);
  }

  public static void startDynamicLayerEditAction(Context context, DynamicLayer dynamicLayer) {
    Bundle bundle = new Bundle();
    bundle.putString(DYNAMIC_LAYER_ID_KEY, dynamicLayer.getId());
    startActionActivity(context, EDIT_DYNAMIC_LAYER, bundle);
  }

  public static void startFreeDrawAction(Context context) {
    startActionActivity(context, FREE_DRAW_ACTION);
  }

  public static void startTimeplayAction(Context context) {
    startActionActivity(context, TIMEPLAY_ACTION);
  }

  private static void startActionActivity(Context context, String action) {
    startActionActivity(context, action, new Bundle());
  }

  private static void startActionActivity(Context context, String action, Bundle bundle) {
    Intent intent = new Intent(context, DrawActionActivity.class);
    intent.putExtra(ACTION_TAG, action);
    intent.putExtras(bundle);
    context.startActivity(intent);
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  @Override
  public void onBackPressed() {
    boolean isConsumed = mFragment.onBackPressed();
    if (!isConsumed) {
      super.onBackPressed();
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String action = getIntent().getStringExtra(ACTION_TAG);
    if (isValidIntentAction(action)) {
      mFragment = setupActionFragment(action);
      setupActionBar(mFragment);
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
        || FREE_DRAW_ACTION.equalsIgnoreCase(action)
        || TIMEPLAY_ACTION.equalsIgnoreCase(action);
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
      return EditDynamicLayerActionFragment.createFragment(getDynamicLayerId());
    } else if (FREE_DRAW_ACTION.equalsIgnoreCase(action)) {
      return new FreeDrawActionFragment();
    } else if (TIMEPLAY_ACTION.equalsIgnoreCase(action)) {
      return new TimeplayActionFragment();
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

  public String getDynamicLayerId() {
    return getIntent().getExtras().getString(DYNAMIC_LAYER_ID_KEY, null);
  }
}

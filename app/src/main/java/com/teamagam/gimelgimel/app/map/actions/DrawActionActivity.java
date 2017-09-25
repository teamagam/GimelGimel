package com.teamagam.gimelgimel.app.map.actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import com.google.common.collect.Iterables;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.activity.BaseActivity;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import javax.inject.Inject;

public class DrawActionActivity extends BaseActivity<GGApplication> {

  private static final Logger sLogger = AppLoggerFactory.create();

  private static final String ACTION_TAG = "action";

  @BindView(R.id.action_toolbar)
  Toolbar mToolbar;
  @Inject
  Iterable<ActionProvider> mActionProviders;

  private BaseDrawActionFragment mFragment;

  public static void startActionActivity(Context context, String action) {
    startActionActivity(context, action, new Bundle());
  }

  public static void startActionActivity(Context context, String action, Bundle bundle) {
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
    getApplicationComponent().inject(this);

    String action = getIntent().getStringExtra(ACTION_TAG);
    if (isValidIntentAction(action)) {
      mFragment = getActionFragment(action);
      setupActionFragment(mFragment);
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
    Iterable<String> availableActions =
        Iterables.transform(mActionProviders, ActionProvider::getActionTag);
    return Iterables.contains(availableActions, action);
  }

  private BaseDrawActionFragment getActionFragment(String action) {
    ActionProvider actionProvider =
        Iterables.find(mActionProviders, ap -> ap.getActionTag().equalsIgnoreCase(action));
    return actionProvider.createActionFragment(getIntent().getExtras());
  }

  private BaseDrawActionFragment setupActionFragment(BaseDrawActionFragment fragment) {
    getSupportFragmentManager().beginTransaction()
        .add(R.id.draw_action_fragment_container, fragment)
        .commit();
    getSupportFragmentManager().executePendingTransactions();
    return fragment;
  }

  private void setupActionBar(BaseDrawActionFragment fragment) {
    setSupportActionBar(mToolbar);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
    setTitle(fragment.getToolbarTitle());
  }
}

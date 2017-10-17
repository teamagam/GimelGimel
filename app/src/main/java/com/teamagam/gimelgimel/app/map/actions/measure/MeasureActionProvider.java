package com.teamagam.gimelgimel.app.map.actions.measure;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class MeasureActionProvider implements ActionProvider {

  private static final String MEASURE_DISTANCE_ACTION = "measure";

  public static void startMeasureAction(Context context) {
    DrawActionActivity.startActionActivity(context, MEASURE_DISTANCE_ACTION);
  }

  @Override
  public String getActionTag() {
    return MEASURE_DISTANCE_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new MeasureActionFragment();
  }
}
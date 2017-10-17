package com.teamagam.gimelgimel.app.map.actions.timeplay;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class TimeplayActionProvider implements ActionProvider {

  private static final String TIMEPLAY_ACTION = "timeplay";

  public static void startTimeplayAction(Context context) {
    DrawActionActivity.startActionActivity(context, TIMEPLAY_ACTION);
  }

  @Override
  public String getActionTag() {
    return TIMEPLAY_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new TimeplayActionFragment();
  }
}
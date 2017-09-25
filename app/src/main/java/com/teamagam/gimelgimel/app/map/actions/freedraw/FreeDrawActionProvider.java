package com.teamagam.gimelgimel.app.map.actions.freedraw;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class FreeDrawActionProvider implements ActionProvider {

  private static final String FREE_DRAW_ACTION = "free_draw";

  public static void startFreeDrawAction(Context context) {
    DrawActionActivity.startActionActivity(context, FREE_DRAW_ACTION);
  }

  @Override
  public String getActionTag() {
    return FREE_DRAW_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new FreeDrawActionFragment();
  }
}
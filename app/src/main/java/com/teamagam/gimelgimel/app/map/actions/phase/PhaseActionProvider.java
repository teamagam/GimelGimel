package com.teamagam.gimelgimel.app.map.actions.phase;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class PhaseActionProvider implements ActionProvider {

  private static final String PHASE_ACTION = "phase";

  public static void startPhaseAction(Context context) {
    DrawActionActivity.startActionActivity(context, PHASE_ACTION);
  }

  @Override
  public String getActionTag() {
    return PHASE_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new PhaseActionFragment();
  }
}

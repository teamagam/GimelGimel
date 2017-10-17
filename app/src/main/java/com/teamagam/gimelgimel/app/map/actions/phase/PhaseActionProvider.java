package com.teamagam.gimelgimel.app.map.actions.phase;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class PhaseActionProvider implements ActionProvider {

  private static final String PHASE_ACTION = "phase";
  private static final String PHASE_LAYER_ID_KEY = "phase_layer_id";

  public static void startPhaseAction(Context context, String phaseLayerId) {
    Bundle bundle = new Bundle();
    bundle.putString(PHASE_LAYER_ID_KEY, phaseLayerId);
    DrawActionActivity.startActionActivity(context, PHASE_ACTION, bundle);
  }

  @Override
  public String getActionTag() {
    return PHASE_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return PhaseActionFragment.newInstance(bundle.getString(PHASE_LAYER_ID_KEY));
  }
}

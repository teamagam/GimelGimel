package com.teamagam.gimelgimel.app.map.actions.send.quadrilateral;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class SendQuadrilateralActionProvider implements ActionProvider {

  private static final String SEND_QUAD_ACTION = "send_quad";

  public static void startSendQuadAction(Context context) {
    DrawActionActivity.startActionActivity(context, SEND_QUAD_ACTION);
  }

  @Override
  public String getActionTag() {
    return SEND_QUAD_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new SendQuadrilateralActionFragment();
  }
}
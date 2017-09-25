package com.teamagam.gimelgimel.app.map.actions.send.geometry;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;

public class SendGeometryActionProvider implements ActionProvider {

  private static final String SEND_GEOMETRY_ACTION = "send_geometry";

  public static void startSendGeometryAction(Context context) {
    DrawActionActivity.startActionActivity(context, SEND_GEOMETRY_ACTION);
  }

  @Override
  public String getActionTag() {
    return SEND_GEOMETRY_ACTION;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return new SendGeometryActionFragment();
  }
}
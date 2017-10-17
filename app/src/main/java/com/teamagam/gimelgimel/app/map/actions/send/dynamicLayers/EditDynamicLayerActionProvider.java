package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import android.content.Context;
import android.os.Bundle;
import com.teamagam.gimelgimel.app.map.actions.ActionProvider;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.DrawActionActivity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;

public class EditDynamicLayerActionProvider implements ActionProvider {

  private static final String EDIT_DYNAMIC_LAYER = "edit_dynamic_layer";
  private static final String DYNAMIC_LAYER_ID_KEY = "dynamic_layer_id_key";

  public static void startDynamicLayerEditAction(Context context, DynamicLayer dynamicLayer) {
    Bundle bundle = new Bundle();
    bundle.putString(DYNAMIC_LAYER_ID_KEY, dynamicLayer.getId());
    DrawActionActivity.startActionActivity(context, EDIT_DYNAMIC_LAYER, bundle);
  }

  @Override
  public String getActionTag() {
    return EDIT_DYNAMIC_LAYER;
  }

  @Override
  public BaseDrawActionFragment createActionFragment(Bundle bundle) {
    return EditDynamicLayerActionFragment.createFragment(getDynamicLayerId(bundle));
  }

  private String getDynamicLayerId(Bundle bundle) {
    return bundle.getString(DYNAMIC_LAYER_ID_KEY, null);
  }
}
package com.teamagam.gimelgimel.app.map.actions;

import android.os.Bundle;

public interface ActionProvider {

  String getActionTag();

  BaseDrawActionFragment createActionFragment(Bundle bundle);
}
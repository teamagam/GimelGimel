package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

abstract class AbsMapAction implements MapAction {

  public void setupSymbologyPanel(EditDynamicLayerViewModel.SymbologyPanelVisibilitySetter setter) {
    setter.hideAll();
  }
}

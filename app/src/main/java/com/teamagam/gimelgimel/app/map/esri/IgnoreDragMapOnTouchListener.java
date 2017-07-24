package com.teamagam.gimelgimel.app.map.esri;

import android.view.MotionEvent;
import com.esri.android.map.MapOnTouchListener;

class IgnoreDragMapOnTouchListener extends MapOnTouchListener {
  IgnoreDragMapOnTouchListener(EsriGGMapView mapView) {
    super(mapView.getContext(), mapView);
  }

  @Override
  public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
    return true;
  }

  @Override
  public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
    return true;
  }
}

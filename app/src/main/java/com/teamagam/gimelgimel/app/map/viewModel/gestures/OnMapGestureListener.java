package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface OnMapGestureListener {

  void onLongPress(PointGeometry pointGeometry);

  void onTap(PointGeometry pointGeometry);
}

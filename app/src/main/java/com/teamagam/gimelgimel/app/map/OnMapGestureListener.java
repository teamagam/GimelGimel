package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface OnMapGestureListener {

  void onLongPress(PointGeometry pointGeometry);

  void onTap(PointGeometry pointGeometry);
}

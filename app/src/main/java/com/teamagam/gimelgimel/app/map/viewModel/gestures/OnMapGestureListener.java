package com.teamagam.gimelgimel.app.map.viewModel.gestures;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;

public interface OnMapGestureListener {

    void onLocationChosen(PointGeometry pointGeometry);
}

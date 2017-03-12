package com.teamagam.gimelgimel.app.map.esri;

import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

public class IntermediateRasterDisplayer {

    private static final AppLogger sLogger = AppLoggerFactory.create();

    private final MapView mMapView;
    private int mLayerPosition;

    private ArcGISLocalTiledLayer mCurrentRasterLayer;

    public IntermediateRasterDisplayer(MapView mapView, int layerPosition) {
        mMapView = mapView;
        mLayerPosition = layerPosition;
    }

    public void display(IntermediateRaster intermediateRaster) {
        mCurrentRasterLayer = createLayer(intermediateRaster);
        mMapView.addLayer(mCurrentRasterLayer, mLayerPosition);
    }

    public void clear() {
        if (mCurrentRasterLayer != null) {
            mMapView.removeLayer(mCurrentRasterLayer);
            mCurrentRasterLayer = null;
        } else {
            sLogger.w("Clear command called without any intermediate raster displayed");
        }
    }

    private ArcGISLocalTiledLayer createLayer(IntermediateRaster intermediateRaster) {
        ArcGISLocalTiledLayer layer =
                new ArcGISLocalTiledLayer(intermediateRaster.getUri().getPath());
        layer.setMaxScale(mMapView.getMaxScale());
        return layer;
    }
}

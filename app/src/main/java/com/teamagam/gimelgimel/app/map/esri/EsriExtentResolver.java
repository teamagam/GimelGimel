package com.teamagam.gimelgimel.app.map.esri;

import com.esri.android.map.ogc.kml.KmlLayer;
import com.esri.core.geometry.Envelope;
import com.teamagam.gimelgimel.domain.layers.VectorLayerExtentResolver;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EsriExtentResolver implements VectorLayerExtentResolver {

  @Inject
  public EsriExtentResolver() {
  }

  @Override
  public Geometry getExtent(VectorLayerPresentation vectorLayer) {
    KmlLayer kmlLayer = new KmlLayer(vectorLayer.getLocalURI().getPath());
    Envelope layerExtent = kmlLayer.getFullExtent();

    return EsriUtils.transformAndProject(layerExtent, kmlLayer.getSpatialReference(),
        EsriUtils.WGS_84_GEO);
  }
}
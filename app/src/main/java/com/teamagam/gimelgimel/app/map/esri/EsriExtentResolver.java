package com.teamagam.gimelgimel.app.map.esri;

import com.esri.android.map.Layer;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ogc.kml.KmlLayer;
import com.esri.core.geometry.Envelope;
import com.teamagam.gimelgimel.domain.layers.VectorLayerExtentResolver;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterExtentResolver;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EsriExtentResolver
    implements VectorLayerExtentResolver, IntermediateRasterExtentResolver {

  @Inject
  public EsriExtentResolver() {
  }

  @Override
  public Geometry getExtent(VectorLayerPresentation vectorLayer) {
    KmlLayer kmlLayer = new KmlLayer(vectorLayer.getLocalURI().getPath());
    return getExtent(kmlLayer);
  }

  @Override
  public Geometry getExtent(IntermediateRasterPresentation irp) {
    TiledLayer tiledLayer = new ArcGISLocalTiledLayer(irp.getUri().getPath());
    return getExtent(tiledLayer);
  }

  private Geometry getExtent(Layer layer) {
    Envelope layerExtent = layer.getFullExtent();
    return EsriUtils.transformAndProject(layerExtent, layer.getDefaultSpatialReference(),
        EsriUtils.WGS_84_GEO);
  }
}
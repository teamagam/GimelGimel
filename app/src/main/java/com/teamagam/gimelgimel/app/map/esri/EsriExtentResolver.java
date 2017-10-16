package com.teamagam.gimelgimel.app.map.esri;

import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.layers.Layer;
import com.teamagam.gimelgimel.domain.layers.VectorLayerExtentResolver;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterExtentResolver;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import java.net.URI;
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
    KmlLayer kmlLayer = new KmlLayer(uriToPath(vectorLayer.getLocalURI()));
    return getExtent(kmlLayer);
  }

  @Override
  public Geometry getExtent(IntermediateRasterPresentation irp) {
    TiledLayer tiledLayer = new ArcGISLocalTiledLayer(uriToPath(irp.getLocalUri()));
    return getExtent(tiledLayer);
  }

  private String uriToPath(URI uri) {
    return uri.getPath();
  }

  private Geometry getExtent(Layer layer) {
    Envelope layerExtent = layer.getFullExtent();
    return EsriUtils.transformAndProject(layerExtent, layer.getDefaultSpatialReference(),
        EsriUtils.WGS_84_GEO);
  }
}
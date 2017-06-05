package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import java.util.List;

public class MapEntityFactory {

  private static final String EMPTY_STRING = "";
  private static int sIdCount = 0;
  private Symbolizer mSymbolizer;

  public MapEntityFactory(Symbolizer symbolizer) {
    mSymbolizer = symbolizer;
  }

  public MapEntityFactory() {
    this(new SimpleSymbolizer());
  }

  public PointEntity createPoint(PointGeometry point) {
    return new PointEntity(generateId(), EMPTY_STRING, point, mSymbolizer.createFromPoint(point));
  }

  public PolylineEntity createPolyline(List<PointGeometry> points) {
    Polyline polyline = new Polyline(points);
    return new PolylineEntity(generateId(), EMPTY_STRING, polyline,
        mSymbolizer.createFromPolyline(points));
  }

  public PolygonEntity createPolygon(List<PointGeometry> points) {
    return new PolygonEntity(generateId(), EMPTY_STRING, new Polygon(points),
        mSymbolizer.createFromPolygon(points));
  }

  private String generateId() {
    return "generatedId_" + sIdCount++;
  }

  public interface Symbolizer {
    PointSymbol createFromPoint(PointGeometry point);

    PolylineSymbol createFromPolyline(List<PointGeometry> points);

    PolygonSymbol createFromPolygon(List<PointGeometry> points);
  }

  public static class SimpleSymbolizer implements Symbolizer {
    @Override
    public PointSymbol createFromPoint(PointGeometry point) {
      return new PointSymbol(false, PointSymbol.POINT_TYPE_CIRCLE);
    }

    @Override
    public PolylineSymbol createFromPolyline(List<PointGeometry> points) {
      return new PolylineSymbol(false);
    }

    @Override
    public PolygonSymbol createFromPolygon(List<PointGeometry> points) {
      return new PolygonSymbol(false);
    }
  }
}

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
    return new PointEntity(generateId(), EMPTY_STRING, point, mSymbolizer.create(point));
  }

  public PolylineEntity createPolyline(List<PointGeometry> points) {
    Polyline polyline = new Polyline(points);
    return new PolylineEntity(generateId(), EMPTY_STRING, polyline, mSymbolizer.create(polyline));
  }

  public PolygonEntity createPolygon(List<PointGeometry> points) {
    Polygon polygon = new Polygon(points);
    return new PolygonEntity(generateId(), EMPTY_STRING, polygon, mSymbolizer.create(polygon));
  }

  private String generateId() {
    return "generatedId_" + sIdCount++;
  }

  public interface Symbolizer {
    PointSymbol create(PointGeometry point);

    PolylineSymbol create(Polyline polyline);

    PolygonSymbol create(Polygon polygon);
  }

  public static class SimpleSymbolizer implements Symbolizer {
    @Override
    public PointSymbol create(PointGeometry point) {
      return new PointSymbol(false);
    }

    @Override
    public PolylineSymbol create(Polyline polyline) {
      return new PolylineSymbol(false);
    }

    @Override
    public PolygonSymbol create(Polygon polygon) {
      return new PolygonSymbol(false);
    }
  }
}

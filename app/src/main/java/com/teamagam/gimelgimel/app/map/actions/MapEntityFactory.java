package com.teamagam.gimelgimel.app.map.actions;

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

  public static final String DEFAULT_SYMBOL_COLOR = "#FFFFFF";
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
    return createPoint(point, mSymbolizer.create(point));
  }

  public PointEntity createPoint(PointGeometry point, PointSymbol symbol) {
    return new PointEntity(generateId(), EMPTY_STRING, point, symbol);
  }

  public PolylineEntity createPolyline(List<PointGeometry> points) {
    return createPolyline(points, mSymbolizer.create(new Polyline(points)));
  }

  public PolylineEntity createPolyline(List<PointGeometry> points, PolylineSymbol symbol) {
    Polyline polyline = new Polyline(points);
    return new PolylineEntity(generateId(), EMPTY_STRING, polyline, symbol);
  }

  public PolygonEntity createPolygon(List<PointGeometry> points) {
    return createPolygon(points, mSymbolizer.create(new Polygon(points)));
  }

  public PolygonEntity createPolygon(List<PointGeometry> points, PolygonSymbol symbol) {
    Polygon polygon = new Polygon(points);
    return new PolygonEntity(generateId(), EMPTY_STRING, polygon, symbol);
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
      return new PointSymbol.PointSymbolBuilder().build();
    }

    @Override
    public PolylineSymbol create(Polyline polyline) {
      return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(DEFAULT_SYMBOL_COLOR)
          .build();
    }

    @Override
    public PolygonSymbol create(Polygon polygon) {
      return new PolygonSymbol.PolygonSymbolBuilder().setBorderColor(DEFAULT_SYMBOL_COLOR)
          .setFillColor(DEFAULT_SYMBOL_COLOR)
          .build();
    }
  }
}

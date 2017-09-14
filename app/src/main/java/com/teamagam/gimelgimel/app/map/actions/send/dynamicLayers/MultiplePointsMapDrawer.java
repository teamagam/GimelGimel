package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.List;

public abstract class MultiplePointsMapDrawer extends AbsMapDrawer {

  protected MultiplePointsMapDrawer(MapEntityFactory mapEntityFactory,
      MapDrawer mapDrawer,
      OnEditingStartListener listener) {
    super(mapEntityFactory, mapDrawer, listener);
  }

  protected GeoEntity buildEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol) {
    if (isOnlyOnePoint(pointsHistory)) {
      return buildPoint(mapEntityFactory, pointsHistory);
    }
    return buildMultipointEntity(mapEntityFactory, pointsHistory, symbol);
  }

  protected abstract GeoEntity buildMultipointEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol);

  private PointEntity buildPoint(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory) {
    return mapEntityFactory.createPoint(pointsHistory.get(0));
  }

  private boolean isOnlyOnePoint(List<PointGeometry> pointsHistory) {
    return pointsHistory.size() == 1;
  }
}

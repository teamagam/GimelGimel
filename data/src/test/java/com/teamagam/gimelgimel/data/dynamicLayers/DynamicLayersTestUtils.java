package com.teamagam.gimelgimel.data.dynamicLayers;

import com.google.common.collect.Lists;
import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicEntityDbEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicEntityMapper;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DynamicLayersTestUtils {

  public static final int TIMESTAMP = 0;
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String DESCRIPTION = "";
  private static final String ICON_ID_1 = "icon_id_1";
  private static final String ICON_TINT_1 = "icon_tint_1";
  private static final String ID_POINT_1 = "id_point_1";
  private static final String ICON_ID_2 = "icon_id_2";
  private static final String ICON_TINT_2 = "icon_tint_2";
  private static final String ID_POINT_2 = "id_point_2";
  private static final double FICTIVE_COORDINATE_1 = 1.0;
  private static final double FICTIVE_COORDINATE_2 = 2.0;

  public static void assertEqualToStrings(Object expected, Object actual) {
    if (expected == null || actual == null) {
      assertEquals(expected, actual);
    } else {
      assertEquals(expected.toString(), actual.toString());
    }
  }

  public static void assertEqualToStrings(Object[] expected, Object[] actual) {
    assertEquals(Arrays.toString(expected), Arrays.toString(actual));
  }

  public static DynamicLayersEntityMapper createDynamicLayersEntityMapper() {
    GeometryDataMapper geometryDataMapper = new GeometryDataMapper();
    SymbolToStyleMapper symbolToStyleMapper = new SymbolToStyleMapper();
    return new DynamicLayersEntityMapper(new DynamicEntityMapper(
        new GeoFeatureEntityMapper(new GeoEntityDataMapper(new GeometryDataMapper()),
            geometryDataMapper, symbolToStyleMapper)));
  }

  public static DynamicLayerEntity createTestLayerEntity() {
    return createTestLayerEntity(ID, NAME);
  }

  public static DynamicLayerEntity createTestLayerEntity(String id, String name) {
    return createTestLayerEntity(id, name, TIMESTAMP);
  }

  public static DynamicLayerEntity createTestLayerEntity(String id, String name, long timestamp) {
    DynamicLayerEntity entity = new DynamicLayerEntity();
    entity.id = id;
    entity.name = name;
    entity.description = DESCRIPTION;
    entity.timestamp = timestamp;

    GeoFeatureEntity ge1 = new GeoFeatureEntity();
    ge1.id = ID_POINT_1;
    ge1.geometry =
        new Point(new SinglePosition(Coordinates.of(FICTIVE_COORDINATE_1, FICTIVE_COORDINATE_1)));
    GeoFeatureEntity.Style style = new GeoFeatureEntity.Style();
    style.iconId = ICON_ID_1;
    style.iconTint = ICON_TINT_1;
    ge1.style = style;

    GeoFeatureEntity ge2 = new GeoFeatureEntity();
    ge2.id = ID_POINT_2;
    ge2.geometry =
        new Point(new SinglePosition(Coordinates.of(FICTIVE_COORDINATE_2, FICTIVE_COORDINATE_2)));
    GeoFeatureEntity.Style style2 = new GeoFeatureEntity.Style();
    style2.iconId = ICON_ID_2;
    style2.iconTint = ICON_TINT_2;
    ge2.style = style2;

    DynamicEntityDbEntity entityDbEntity1 = new DynamicEntityDbEntity();
    entityDbEntity1.geoFeatureEntity = ge1;
    entityDbEntity1.description = DESCRIPTION;
    DynamicEntityDbEntity entityDbEntity2 = new DynamicEntityDbEntity();
    entityDbEntity2.geoFeatureEntity = ge2;
    entityDbEntity2.description = DESCRIPTION;
    entity.entities = new DynamicEntityDbEntity[] { entityDbEntity1, entityDbEntity2 };
    return entity;
  }

  public static DynamicLayer createTestLayer() {
    PointSymbol s1 =
        new PointSymbol.PointSymbolBuilder().setTintColor(ICON_TINT_1).setIconId(ICON_ID_1).build();
    PointGeometry pg1 = new PointGeometry(FICTIVE_COORDINATE_1, FICTIVE_COORDINATE_1);
    PointEntity pe1 = new PointEntity(ID_POINT_1, pg1, s1);
    PointSymbol s2 =
        new PointSymbol.PointSymbolBuilder().setTintColor(ICON_TINT_2).setIconId(ICON_ID_2).build();
    PointGeometry pg2 = new PointGeometry(FICTIVE_COORDINATE_2, FICTIVE_COORDINATE_2);
    PointEntity pe2 = new PointEntity(ID_POINT_2, pg2, s2);
    return createTestLayer(Arrays.asList(pe1, pe2));
  }

  public static DynamicLayer createNullEntitiesTestLayer() {
    return new DynamicLayer(ID, NAME, DESCRIPTION, TIMESTAMP, null);
  }

  public static DynamicLayer createTestLayer(List<GeoEntity> entities) {
    return new DynamicLayer(ID, NAME, DESCRIPTION, TIMESTAMP, toDynamicEntities(entities));
  }

  private static List<DynamicEntity> toDynamicEntities(List<GeoEntity> entities) {
    return Lists.transform(entities, e -> new DynamicEntity(e, DESCRIPTION));
  }
}

package com.teamagam.gimelgimel.data.dynamicLayers;

import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DynamicLayersTestUtils {

  public static final String ID = "id";
  public static final String NAME = "name";
  public static final String TEXT_POINT_1 = "text_1";
  public static final String ICON_ID_1 = "icon_id_1";
  public static final String ICON_TINT_1 = "icon_tint_1";
  public static final String ID_POINT_1 = "id_point_1";
  public static final String TEXT_POINT_2 = "text_2";
  public static final String ICON_ID_2 = "icon_id_2";
  public static final String ICON_TINT_2 = "icon_tint_2";
  public static final String ID_POINT_2 = "id_point_2";
  public static final double FICTIVE_COORDINATE_1 = 1.0;
  public static final double FICTIVE_COORDINATE_2 = 2.0;

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
    return new DynamicLayersEntityMapper(
        new GeoFeatureEntityMapper(new GeoEntityDataMapper(geometryDataMapper), geometryDataMapper,
            symbolToStyleMapper));
  }

  public static DynamicLayerEntity createTestEntity() {
    return createTestEntity(ID, NAME);
  }

  public static DynamicLayerEntity createTestEntity(String id, String name, long timestamp) {
    DynamicLayerEntity entity = new DynamicLayerEntity();
    entity.id = id;
    entity.name = name;
    entity.timestamp = timestamp;

    GeoFeatureEntity ge1 = new GeoFeatureEntity();
    ge1.id = ID_POINT_1;
    ge1.geometry =
        new Point(new SinglePosition(Coordinates.of(FICTIVE_COORDINATE_1, FICTIVE_COORDINATE_1)));
    ge1.text = TEXT_POINT_1;
    GeoFeatureEntity.Style style = new GeoFeatureEntity.Style();
    style.iconId = ICON_ID_1;
    style.iconTint = ICON_TINT_1;
    ge1.style = style;

    GeoFeatureEntity ge2 = new GeoFeatureEntity();
    ge2.id = ID_POINT_2;
    ge2.geometry =
        new Point(new SinglePosition(Coordinates.of(FICTIVE_COORDINATE_2, FICTIVE_COORDINATE_2)));
    ge2.text = TEXT_POINT_2;
    GeoFeatureEntity.Style style2 = new GeoFeatureEntity.Style();
    style2.iconId = ICON_ID_2;
    style2.iconTint = ICON_TINT_2;
    ge2.style = style2;

    entity.entities = new GeoFeatureEntity[] { ge1, ge2 };
    return entity;
  }

  public static DynamicLayerEntity createTestEntity(String id, String name) {
    return createTestEntity(id, name, 0);
  }

  public static DynamicLayer createTestLayer() {
    PointSymbol s1 =
        new PointSymbol.PointSymbolBuilder().setTintColor(ICON_TINT_1).setIconId(ICON_ID_1).build();
    PointGeometry pg1 = new PointGeometry(FICTIVE_COORDINATE_1, FICTIVE_COORDINATE_1);
    PointEntity pe1 = new PointEntity(ID_POINT_1, TEXT_POINT_1, pg1, s1);
    PointSymbol s2 =
        new PointSymbol.PointSymbolBuilder().setTintColor(ICON_TINT_2).setIconId(ICON_ID_2).build();
    PointGeometry pg2 = new PointGeometry(FICTIVE_COORDINATE_2, FICTIVE_COORDINATE_2);
    PointEntity pe2 = new PointEntity(ID_POINT_2, TEXT_POINT_2, pg2, s2);
    return createTestLayer(Arrays.asList(pe1, pe2));
  }

  public static DynamicLayer createTestLayer(List<GeoEntity> entities) {
    return new DynamicLayer(ID, NAME, 0, entities);
  }
}

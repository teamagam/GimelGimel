package com.teamagam.gimelgimel.data.message.repository.cache.room.mappers;

import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.IconData;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.Style;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeoFeatureEntityMapper implements EntityMapper<GeoEntity, GeoFeatureEntity> {

  private GeoEntityDataMapper mGeoEntityDataMapper;
  private GeometryDataMapper mGeometryDataMapper;
  private SymbolToStyleMapper mSymbolToStyleMapper;

  @Inject
  public GeoFeatureEntityMapper(GeoEntityDataMapper geoEntityDataMapper,
      GeometryDataMapper geometryDataMapper,
      SymbolToStyleMapper symbolToStyleMapper) {
    mGeoEntityDataMapper = geoEntityDataMapper;
    mGeometryDataMapper = geometryDataMapper;
    mSymbolToStyleMapper = symbolToStyleMapper;
  }

  @Override
  public GeoEntity mapToDomain(GeoFeatureEntity entity) {
    IconData iconData = new IconData(entity.style.iconId, entity.style.iconTint);
    Style style = new Style(iconData, entity.style.borderColor, entity.style.fillColor,
        entity.style.borderStyle);
    return mGeoEntityDataMapper.transform(entity.id,
        new GeoContentData(entity.geometry, entity.text, style));
  }

  @Override
  public GeoFeatureEntity mapToEntity(GeoEntity geoEntity) {
    GeoFeatureEntity entity = new GeoFeatureEntity();
    entity.id = geoEntity.getId();
    entity.geometry = mGeometryDataMapper.transformToDataBySuperclass(geoEntity.getGeometry());
    entity.text = geoEntity.getText();
    entity.style = mSymbolToStyleMapper.transform(geoEntity.getSymbol());
    return entity;
  }
}

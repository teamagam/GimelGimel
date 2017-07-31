package com.teamagam.gimelgimel.data.dynamicLayers.room.mapper;

import com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils;
import com.teamagam.gimelgimel.data.dynamicLayers.room.entities.DynamicLayerEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import org.junit.Before;
import org.junit.Test;

import static com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils.assertEqualToStrings;
import static org.junit.Assert.assertNull;

public class DynamicLayersEntityMapperTest {

  private DynamicLayersEntityMapper mMapper;
  private DynamicLayerEntity mEntity;
  private DynamicLayer mDynamicLayer;

  @Before
  public void setUp() {
    mMapper = DynamicLayersTestUtils.createDynamicLayersEntityMapper();
    mDynamicLayer = DynamicLayersTestUtils.createTestLayer();
    mEntity = DynamicLayersTestUtils.createTestEntity();
  }

  @Test
  public void mapToDomainTest_NullCase() {
    assertNull(mMapper.mapToDomain(null));
  }

  @Test
  public void mapToEntityTest_NullCase() {
    assertNull(mMapper.mapToEntity(null));
  }

  @Test
  public void mapToDomainTest_NullEntitiesCase() {
    // Arrange
    mEntity.entities = null;
    mDynamicLayer = DynamicLayersTestUtils.createTestLayer(null);

    // Assert
    assertEqualToStrings(mDynamicLayer, mMapper.mapToDomain(mEntity));
  }

  @Test
  public void mapToEntityTest_NullEntitiesCase() {
    // Arrange
    mEntity.entities = null;
    mDynamicLayer = DynamicLayersTestUtils.createTestLayer(null);

    // Assert
    assertEqualToStrings(mEntity, mMapper.mapToEntity(mDynamicLayer));
  }

  @Test
  public void mapToDomainTest() {
    assertEqualToStrings(mDynamicLayer, mMapper.mapToDomain(mEntity));
  }

  @Test
  public void mapToEntityTest() {
    assertEqualToStrings(mEntity, mMapper.mapToEntity(mDynamicLayer));
  }
}
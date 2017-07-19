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
  public void mapToDomainTestNullCase() {
    assertNull(mMapper.mapToDomain(null));
  }

  @Test
  public void mapToEntityTestNullCase() {
    assertNull(mMapper.mapToEntity(null));
  }

  @Test
  public void mapToDomainTest() {
    assertEqualToStrings(mDynamicLayer, mMapper.mapToDomain(mEntity));
  }

  @Test
  public void mapToEntityTest() {
    assertEqualToStrings(mMapper.mapToEntity(mDynamicLayer), mEntity);
  }
}
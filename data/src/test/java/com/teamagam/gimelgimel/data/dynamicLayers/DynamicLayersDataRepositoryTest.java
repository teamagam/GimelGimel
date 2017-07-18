package com.teamagam.gimelgimel.data.dynamicLayers;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DynamicLayersDataRepositoryTest {

  public static final String ID_1 = "id";
  public static final String ID_2 = "id2";

  private DynamicLayersDataRepository mRepo;
  private DynamicLayer mLayer1;
  private DynamicLayer mLayer2;

  @Before
  public void setUp() {
    mRepo = new DynamicLayersDataRepository();
    mLayer1 = new DynamicLayer(ID_1, "name", Collections.EMPTY_LIST);
    mLayer2 = new DynamicLayer(ID_2, "name2", Collections.EMPTY_LIST);
  }

  @Test
  public void canRetrieveLayers() {
    // Arrange

    // Act
    mRepo.put(mLayer1);
    mRepo.put(mLayer2);

    // Assert
    assertEquals(mLayer1, mRepo.getById(ID_1));
    assertEquals(mLayer2, mRepo.getById(ID_2));
  }

  @Test(expected = RuntimeException.class)
  public void nonExistingLayerThrowsException() {
    // Arrange

    // Act
    mRepo.getById(ID_1);
  }

  @Test
  public void repoContainsOnlyPutLayer() {
    // Arrange

    // Act
    mRepo.put(mLayer1);

    // Assert
    assertTrue(mRepo.contains(ID_1));
    assertFalse(mRepo.contains(ID_2));
  }
}
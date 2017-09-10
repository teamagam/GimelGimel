package com.teamagam.gimelgimel.data.timeplay.dynamic_layers;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.dynamicLayers.room.mapper.DynamicLayersEntityMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DynamicLayersSnapshoterTest extends BaseTest {

  public static final String NAME = "name";
  private static final String DESCRIPTION = "";
  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  @Rule
  public DbInitializerRule dbRule = new DbInitializerRule();

  private DynamicLayerDao mDynamicLayerDao;
  private DynamicLayersSnapshoter mSnapshoter;
  private DynamicLayersEntityMapper mMapper;

  private void insertDynamicLayer(String id, int timestamp, String... geoEntityIds) {
    List<DynamicEntity> dynamicEntities = new ArrayList<>();
    for (String geoEntityId : geoEntityIds) {
      dynamicEntities.add(new DynamicEntity(getEntity(geoEntityId), DESCRIPTION));
    }
    DynamicLayer dl = new DynamicLayer(id, NAME, DESCRIPTION, timestamp, dynamicEntities);
    mDynamicLayerDao.insertDynamicLayer(mMapper.mapToEntity(dl));
  }

  private GeoEntity getEntity(String id) {
    return new PointEntity(id, new PointGeometry(1, 1),
        new PointSymbol.PointSymbolBuilder().build());
  }

  private void assertSnapshotContainIds(List<GeoEntity> snapshot, String... ids) {
    assertThat(Lists.transform(snapshot, GeoEntity::getId), containsInAnyOrder(ids));
  }

  @Before
  public void setUp() throws Exception {
    mDynamicLayerDao = dbRule.getDb().dynamicLayerDao();

    mMapper = DynamicLayersTestUtils.createDynamicLayersEntityMapper();

    mSnapshoter = new DynamicLayersSnapshoter(mDynamicLayerDao, mMapper);
  }

  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void getAllEntitiesOutOfSingleLayer() throws Exception {
    //Arrange
    String id_1 = "id_1";
    String id_2 = "id_2";
    String dl_id = "id";
    int dl_timestamp = 5;
    insertDynamicLayer(dl_id, dl_timestamp, id_1, id_2);

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(dl_timestamp);

    //Assert
    assertSnapshotContainIds(snapshot, id_1, id_2);
  }

  @Test
  public void getLatestSuitableVersion() throws Exception {
    //Arrange
    String dl_id = "dl_id";
    insertDynamicLayer(dl_id, 1, "id1", "id2");
    insertDynamicLayer(dl_id, 5, "id3", "id4");
    insertDynamicLayer(dl_id, 10, "id5", "id6");

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(6);

    //Assert
    assertSnapshotContainIds(snapshot, "id3", "id4");
  }
}
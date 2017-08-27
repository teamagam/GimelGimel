package com.teamagam.gimelgimel.data.timeplay;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AggregationGeoSnapshoterTest extends BaseTest {

  private AggregationGeoSnapshoter mAggregationGeoSnapshoter;
  private GeoSnapshoter mSnapshoter1;
  private GeoSnapshoter mSnapshoter2;

  @Before
  public void setUp() throws Exception {
    mSnapshoter1 = mock(GeoSnapshoter.class);
    mSnapshoter2 = mock(GeoSnapshoter.class);

    mAggregationGeoSnapshoter = new AggregationGeoSnapshoter(mSnapshoter1, mSnapshoter2);
  }

  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mAggregationGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void retrievesCombinedResult() throws Exception {
    //Arrange
    List<GeoEntity> snapshot1 = getGeoEntities("id1", "id2");
    when(mSnapshoter1.snapshot(5)).thenReturn(snapshot1);
    List<GeoEntity> snapshot2 = getGeoEntities("id3", "id4");
    when(mSnapshoter2.snapshot(5)).thenReturn(snapshot2);

    //Act
    List<GeoEntity> snapshot = mAggregationGeoSnapshoter.snapshot(5);

    //Assert
    assertThat(snapshot, hasSize(4));
    String[] ids = new String[] { "id1", "id2", "id3", "id4" };
    assertThat(Lists.transform(snapshot, GeoEntity::getId), containsInAnyOrder(ids));
  }

  private List<GeoEntity> getGeoEntities(String... ids) {
    List<GeoEntity> res = new ArrayList<>(ids.length);
    for (String id : ids) {
      GeoEntity entity = mock(GeoEntity.class);
      when(entity.getId()).thenReturn(id);
      res.add(entity);
    }

    return res;
  }
}
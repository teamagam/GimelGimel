package com.teamagam.gimelgimel.data.timeplay;

import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
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

public class DataGeoSnapshoterTest extends BaseTest {

  private DataGeoSnapshoter mDataGeoSnapshoter;
  private UserGeoSnapshoter mUsersSnapshoterMock;
  private GeoEntitiesSnapshoter mGeoEntitiesSnapshoterMock;

  @Before
  public void setUp() throws Exception {
    mUsersSnapshoterMock = mock(UserGeoSnapshoter.class);
    mGeoEntitiesSnapshoterMock = mock(GeoEntitiesSnapshoter.class);

    mDataGeoSnapshoter = new DataGeoSnapshoter(mUsersSnapshoterMock, mGeoEntitiesSnapshoterMock);
  }

  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mDataGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void retrievesCombinedResult() throws Exception {
    //Arrange
    List<GeoEntity> usersReturns = getGeoEntities("id1", "id2");
    when(mUsersSnapshoterMock.snapshot(5)).thenReturn(usersReturns);
    List<GeoEntity> geoReturns = getGeoEntities("id3", "id4");
    when(mGeoEntitiesSnapshoterMock.snapshot(5)).thenReturn(geoReturns);
    //Act
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(5);

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
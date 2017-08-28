package com.teamagam.gimelgimel.data.timeplay.messages;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.common.DbInitializerRule;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.data.timeplay.Utils;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class GeoMessagesSnapshoterTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  @Rule
  public DbInitializerRule mDbRule = new DbInitializerRule();

  private MessagesDao mMessagesDao;
  private GeoMessagesSnapshoter mSnapshoter;

  private GeoFeatureEntityMapper getGeoFeatureEntityMapper() {
    return new GeoFeatureEntityMapper(new GeoEntityDataMapper(new GeometryDataMapper()),
        new GeometryDataMapper(), new SymbolToStyleMapper());
  }

  private void insertGeoMessage(String id, long timestamp) {
    mMessagesDao.insertMessage(Utils.createGeoChatMessageEntity(id, timestamp));
  }

  @Before
  public void setUp() throws Exception {
    mMessagesDao = mDbRule.getDb().messageDao();
    mSnapshoter = new GeoMessagesSnapshoter(mMessagesDao, getGeoFeatureEntityMapper());
  }


  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void retrievesGeoMessage() throws Exception {
    //Arrange
    insertGeoMessage("id1", 0);

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is("id1"));
  }

  @Test
  public void filtersNewerMessages() throws Exception {
    //Arrange
    insertGeoMessage("id1", 0);
    insertGeoMessage("id2", 1);

    //Act
    List<GeoEntity> snapshot = mSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is("id1"));
  }
}
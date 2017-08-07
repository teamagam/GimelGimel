package com.teamagam.gimelgimel.data.timeplay;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.content.Context;
import com.google.common.collect.Lists;
import com.teamagam.geogson.core.model.Coordinates;
import com.teamagam.geogson.core.model.Point;
import com.teamagam.geogson.core.model.positions.SinglePosition;
import com.teamagam.gimelgimel.data.dynamicLayers.DynamicLayersTestUtils;
import com.teamagam.gimelgimel.data.map.adapter.GeoEntityDataMapper;
import com.teamagam.gimelgimel.data.map.adapter.GeometryDataMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.ChatMessageEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.GeoFeatureEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.LocationSampleEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.entities.UserLocationEntity;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.GeoFeatureEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.SymbolToStyleMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.UserLocationsEntityMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
public class DataGeoSnapshoterTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

  private UserLocationDao mUserLocationDao;
  private MessagesDao mMessagesDao;
  private DataGeoSnapshoter mDataGeoSnapshoter;
  private AppDatabase mDb;

  @Before
  public void setUp() throws Exception {
    Context context = RuntimeEnvironment.application.getApplicationContext();
    mDb = DynamicLayersTestUtils.getDB(context);
    mUserLocationDao = mDb.userLocationDao();
    mMessagesDao = mDb.messageDao();

    mDataGeoSnapshoter =
        new DataGeoSnapshoter(mUserLocationDao, mMessagesDao, getGeoFeatureEntityMapper(),
            getUserLocationsEntityMapper());
  }

  @After
  public void tearDown() throws Exception {
    mDb.close();
  }

  @Test
  public void onEmptyDb_expectEmptyResult() throws Exception {
    //Act
    Collection<GeoEntity> res = mDataGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(res, empty());
  }

  @Test
  public void retrievesGeoMessage() throws Exception {
    //Arrange
    insertGeoMessage("id1", 0);

    //Act
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is("id1"));
  }

  @Test
  public void retrievesUserLocation() throws Exception {
    //Arrange
    insertUserLocation(0, "id1");

    //Act
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(0);

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
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is("id1"));
  }

  @Test
  public void filterNewerUserLocations() throws Exception {
    //Arrange
    insertUserLocation(0, "id1");
    insertUserLocation(1, "id2");

    //Act
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(0);

    //Assert
    assertThat(snapshot, hasSize(1));
    assertThat(snapshot.get(0).getId(), is("id1"));
  }

  @Test
  public void retrievesFilteredCombinedResult() throws Exception {
    //Arrange
    insertGeoMessage("mid1", 0);
    insertUserLocation(1, "uid1");
    insertGeoMessage("mid2", 10);
    insertUserLocation(11, "uid2");

    //Act
    List<GeoEntity> snapshot = mDataGeoSnapshoter.snapshot(5);

    //Assert
    assertThat(snapshot, hasSize(2));

    assertThat(Lists.transform(snapshot, GeoEntity::getId), containsInAnyOrder("mid1", "uid1"));
  }

  private GeoFeatureEntityMapper getGeoFeatureEntityMapper() {
    return new GeoFeatureEntityMapper(new GeoEntityDataMapper(new GeometryDataMapper()),
        new GeometryDataMapper(), new SymbolToStyleMapper());
  }

  private UserLocationsEntityMapper getUserLocationsEntityMapper() {
    return new UserLocationsEntityMapper(new GeometryDataMapper());
  }

  private void insertGeoMessage(String id1, int creationTimestamp) {
    mMessagesDao.insertMessage(createGeoChatMessageEntity(id1, creationTimestamp));
  }

  private ChatMessageEntity createGeoChatMessageEntity(String geoEntityId, int creationTimestamp) {
    ChatMessageEntity res = new ChatMessageEntity();
    res.messageId = generateRandomId();
    res.senderId = "fake";
    res.creationDate = new Date(creationTimestamp);
    res.geoFeatureEntity = new GeoFeatureEntity();
    res.geoFeatureEntity.geometry = createPoint();
    GeoFeatureEntity.Style style = new GeoFeatureEntity.Style();
    style.iconId = "icon_id";
    res.geoFeatureEntity.style = style;
    res.geoFeatureEntity.id = geoEntityId;
    res.geoFeatureEntity.style = style;
    res.geoFeatureEntity.text = "geo_text";
    return res;
  }

  private Point createPoint() {
    return new Point(new SinglePosition(Coordinates.of(30, 30)));
  }

  private String generateRandomId() {
    return UUID.randomUUID().toString();
  }

  private void insertUserLocation(int timestamp, String userId) {
    mUserLocationDao.insertUserLocation(createUserLocationEntity(timestamp, userId));
  }

  private UserLocationEntity createUserLocationEntity(long timestamp, String userId) {
    UserLocationEntity userLocationEntity = new UserLocationEntity();
    userLocationEntity.id = generateRandomId();
    userLocationEntity.user = userId;
    LocationSampleEntity location = new LocationSampleEntity();
    location.time = timestamp;
    location.point = createPoint();
    userLocationEntity.location = location;
    return userLocationEntity;
  }
}
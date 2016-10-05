package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.Symbol;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

import rx.observers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DisplayedEntitiesDataRepositoryTest {

    private GeoEntity createGeoEntity(String id) {
        return new BaseGeoEntity(id, new PointGeometry(0, 0), new Symbol() {
        }, "layer1");
    }

    private DisplayedEntitiesRepository mDisplayedRepo;
    private TestSubscriber<GeoEntityNotification> mSyncTestSubscriber;
    private TestSubscriber<Collection<GeoEntity>> mGetTestSubscriber;

    @Before
    public void setUp() throws Exception {
        mDisplayedRepo = new DisplayedEntitiesDataRepository();
        mSyncTestSubscriber = new TestSubscriber<>();
        mGetTestSubscriber = new TestSubscriber<>();
    }

    @Test
    public void show_subscribeSyncBefore_shouldOnlyEmitAddedGeoEntity() throws Exception {
        //Arrange
        mDisplayedRepo.getSyncEntitiesObservable().subscribe(mSyncTestSubscriber);
        GeoEntity geoEntity = createGeoEntity("geometry");

        //Act
        mDisplayedRepo.show(geoEntity);

        //Assert
        List<GeoEntityNotification> onNextEvents = mSyncTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(1));
        assertThat(onNextEvents.get(0).getGeoEntity(), is(geoEntity));
        assertThat(onNextEvents.get(0).getAction(), is(GeoEntityNotification.ADD));
        mSyncTestSubscriber.assertNoErrors();
    }


    @Test
    public void show_subscribeSyncAfter_shouldNotEmitChanges() throws Exception {
        //Arrange
        GeoEntity geoEntity = createGeoEntity("geometry");

        //Act
        mDisplayedRepo.show(geoEntity);
        mDisplayedRepo.getSyncEntitiesObservable().subscribe(mSyncTestSubscriber);

        //Assert
        List<GeoEntityNotification> onNextEvents = mSyncTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(0));
        mSyncTestSubscriber.assertNoErrors();
    }

    @Test
    public void hide_showThenHideSubscribeGetAfter_shouldNotEmitGeoEntity() throws Exception {
        //Arrange
        GeoEntity geoEntity = createGeoEntity("geometry");

        //Act
        mDisplayedRepo.show(geoEntity);
        mDisplayedRepo.hide(geoEntity);
        mDisplayedRepo.getDisplayedGeoEntitiesObservable().subscribe(mGetTestSubscriber);

        //Assert
        List<Collection<GeoEntity>> onNextEvents = mGetTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.get(0).size(), is(0));
        mSyncTestSubscriber.assertNoErrors();
    }

    @Test
    public void hide_showThenHideSubscribeSyncBefore_shouldEmitTwoNotifications() throws Exception {
        //Arrange
        GeoEntity geoEntity = createGeoEntity("geometry");
        mDisplayedRepo.getSyncEntitiesObservable().subscribe(mSyncTestSubscriber);

        //Act
        mDisplayedRepo.show(geoEntity);
        mDisplayedRepo.hide(geoEntity);

        //Assert
        List<GeoEntityNotification> onNextEvents = mSyncTestSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(2));
        assertThat(onNextEvents.get(0).getAction(), is(GeoEntityNotification.ADD));
        assertThat(onNextEvents.get(1).getAction(), is(GeoEntityNotification.REMOVE));
        mSyncTestSubscriber.assertNoErrors();
    }

    @Test
    public void getDisplayedGeoEntitiesObservable_subscribeAfter_shouldEmitGeoEntity() throws Exception {
        //Arrange
        GeoEntity geoEntity = createGeoEntity("geometry");
        mDisplayedRepo.show(geoEntity);

        //Act
        mDisplayedRepo.getDisplayedGeoEntitiesObservable().subscribe(mGetTestSubscriber);

        //Assert
        List<Collection<GeoEntity>> geoEntities = mGetTestSubscriber.getOnNextEvents();

        assertThat(geoEntities.size(), is(1));
        assertThat(geoEntities.get(0).iterator().next(), is(geoEntity));

        mGetTestSubscriber.assertNoErrors();
    }

    @Test
    public void getDisplayedGeoEntitiesObservable_subscribeBefore_shouldNotEmitGeoEntity() {
        //Arrange
        GeoEntity geoEntity = createGeoEntity("geometry");
        mDisplayedRepo.getDisplayedGeoEntitiesObservable().subscribe(mGetTestSubscriber);

        //Act
        mDisplayedRepo.show(geoEntity);

        //Assert
        List<Collection<GeoEntity>> geoEntities = mGetTestSubscriber.getOnNextEvents();

        assertThat(geoEntities.get(0).size(), is(0));

        mGetTestSubscriber.assertNoErrors();
    }

}

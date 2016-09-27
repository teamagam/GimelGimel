package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.BaseGeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.Symbol;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.observables.ConnectableObservable;
import rx.observers.TestSubscriber;
import rx.subjects.PublishSubject;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DisplayedEntitiesDataRepositoryTest {

    private GeoEntity mGeoEntity =
            new BaseGeoEntity("test1", new PointGeometry(0, 0), new Symbol() {
            });
    private GeoEntity mGeoEntity2 =
            new BaseGeoEntity("test2", new PointGeometry(0, 0), new Symbol() {
            });

    private DisplayedEntitiesDataRepository mDisplayedRepo;
    private TestSubscriber<GeoEntityNotification> testSubscriber;
    private Observable<GeoEntityNotification> mNotificationObservable;
    private Observable<GeoEntityNotification> mNewObservable;

    @Before
    public void setUp() throws Exception {
        mDisplayedRepo = new DisplayedEntitiesDataRepository();
        mNotificationObservable = mDisplayedRepo.getSyncEntitiesObservable();
        testSubscriber = new TestSubscriber<>();
    }

    @Test
    public void addEntity_subscribeBefore_shouldEmitOnce() throws Exception {
        mNotificationObservable.subscribe(testSubscriber);
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents.size(), is(1));

        assertThat(onNextEvents.get(0).getGeoEntity(), is(mGeoEntity));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void addEntity_subscribeAfter_shouldZero() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");
        mNotificationObservable.subscribe(testSubscriber);

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents.size(), is(0));

        assertThat(onNextEvents.get(0).getGeoEntity(), is(mGeoEntity));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void getDisplayedVectorLayerObservable_subscribeAfter_shouldEmitTwice() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");
        mDisplayedRepo.addEntity(mGeoEntity2, "layer1");

        mNewObservable = mDisplayedRepo.getDisplayedVectorLayerObservable();
        mNewObservable.subscribe(testSubscriber);

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents.size(), is(2));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void getDisplayedVectorLayerObservable_subscribeBefore_shouldEmitTwice() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");

        mNewObservable = mDisplayedRepo.getDisplayedVectorLayerObservable();
        mNewObservable.subscribe(testSubscriber);

        mDisplayedRepo.addEntity(mGeoEntity2, "layer1");

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();

        assertThat(onNextEvents.size(), is(2));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void getDisplayedVectorLayerObservable_twoSubscriptions_shouldEmitThree() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");

        mNewObservable = mDisplayedRepo.getDisplayedVectorLayerObservable();
        mNewObservable.subscribe(testSubscriber);

        mNotificationObservable.subscribe(testSubscriber);

        mDisplayedRepo.addEntity(mGeoEntity2, "layer1");

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(3));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void removeEntity_getAll_shouldEmitOnce() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");
        mDisplayedRepo.removeEntity(mGeoEntity.getId(), "layer1");

        mDisplayedRepo.addEntity(mGeoEntity2, "layer1");

        mNewObservable = mDisplayedRepo.getDisplayedVectorLayerObservable();
        mNewObservable.subscribe(testSubscriber);

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(1));

        testSubscriber.assertNoErrors();
    }


    @Test
    public void removeEntity_sync_shouldEmitOnce() throws Exception {
        mDisplayedRepo.addEntity(mGeoEntity, "layer1");

        mNotificationObservable.subscribe(testSubscriber);

        mDisplayedRepo.removeEntity(mGeoEntity.getId(), "layer1");

        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();
        assertThat(onNextEvents.size(), is(1));

        testSubscriber.assertNoErrors();
    }

    @Test
    public void qwe() throws Exception {
        ReplaySubject<String> p = ReplaySubject.create();
//        ps.map(s -> s + "kkk" );
//        Observable<String> p = ps.share();

//        ConnectableObservable<String> p = p.publish();

        Observable<String> o = p.map(s -> s + "1");

        o = o.doOnNext(System.out::println);

//        p.onNext("v");

        o = o.doOnNext(System.out::println);

        o.subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object s) {
                System.out.println(s + "on");
            }
        });

        p.onNext("tttttttttt");

        o = o.doOnNext(System.out::println);

//        ps.onNext("t");
//
//        p.doOnNext(System.out::println);

//        p.connect();
//        List<GeoEntityNotification> onNextEvents = testSubscriber.getOnNextEvents();
//
//        assertThat(onNextEvents.size(), is(1));
//
//        testSubscriber.assertNoErrors();
    }

//    @Test
//    public void getSyncEntitiesObservable_emitOne_shouldEmitOne() throws Exception {
//        assertThat(res, equalTo(0L));
//    }
//
//    public Observable<GeoEntityNotification> getSyncEntitiesObservable() {
//        return mSubject;
//    }
//
//    public Observable<VectorLayer> getDisplayedVectorLayerObservable() {
//        return Observable.just(displayedVectorLayer);
//    }
//
//    public void addEntity(GeoEntity geoEntity, String vectorLayerId){
//        displayedVectorLayer.addEntity(geoEntity);
//        mSubject.onNext(createGeoEntityNotification(geoEntity, vectorLayerId, GeoEntityNotification.ADD));
//    }
//
//    public void removeEntity(String entityId, String vectorLayerId){
//        GeoEntity geoEntity = displayedVectorLayer.removeEntity(entityId);
//        mSubject.onNext(createGeoEntityNotification(geoEntity, vectorLayerId,
//                GeoEntityNotification.REMOVE));
//    }
//
//    public void updateEntity(GeoEntity geoEntity, String vectorLayerId){
//        removeEntity(geoEntity.getId(), vectorLayerId);
//        addEntity(geoEntity, vectorLayerId);
//    }
//
//    private GeoEntityNotification createGeoEntityNotification(GeoEntity geoEntity, String vectorLayerId, int action) {
//        return new GeoEntityNotification(geoEntity, vectorLayerId, action);
//    }
}

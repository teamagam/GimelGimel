package com.teamagam.gimelgimel.domain.location;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

import rx.Observable;
import rx.plugins.RxJavaSchedulersHook;
import rx.subjects.SerializedSubject;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SendSelfLocationsInteractorTest extends BaseTest {

    private SendSelfLocationsInteractor mSubject;
    private CountDownLatch mCDL;
    private SerializedSubject<LocationSample, LocationSample> mLocationsSubject;


    @Before
    public void setUp() throws Exception {
        UserPreferencesRepository userPreferences = mock(UserPreferencesRepository.class);
        MessagesRepository messagesRepository = mock(MessagesRepository.class);
        when(messagesRepository.sendMessage(any())).thenReturn(Observable.just(mock(Message.class)
        ).doOnNext(m -> mCDL.countDown()));
        LocationRepository locationRepository = mock(LocationRepository.class);
        mLocationsSubject = new SerializedSubjectBuilder().build();
        when(locationRepository.getLocationObservable()).thenReturn(mLocationsSubject);
        ThreadExecutor threadExecutor = RxJavaSchedulersHook::createComputationScheduler;
        mSubject = new SendSelfLocationsInteractor(
                threadExecutor,
                userPreferences,
                messagesRepository,
                locationRepository);

        mCDL = new CountDownLatch(1);
    }

    @Test
    public void name() throws Exception {
        mSubject.execute();
        mLocationsSubject.onNext(mock(LocationSample.class));
        mCDL.await();
    }
}
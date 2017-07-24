package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendRemoteAddDynamicEntityRequestInteractorTest extends BaseTest {

  private SendRemoteAddDynamicEntityRequestInteractor mTestSubject;
  private RemoteDynamicLayerSourceHandler mRemoteSourceMock;
  private GeoEntity mGeoEntityMock;
  private DynamicLayer mSingleDynamicLayer;

  @Before
  public void setUp() throws Exception {
    DynamicLayersRepository dynamicLayersRepositoryMock = mock(DynamicLayersRepository.class);
    mSingleDynamicLayer =
        new DynamicLayer("dynamic_layer_id", "dynamic_layer_name", new ArrayList<>());
    when(dynamicLayersRepositoryMock.getObservable()).thenReturn(
        Observable.just(mSingleDynamicLayer));

    mRemoteSourceMock = spy(RemoteDynamicLayerSourceHandler.class);
    mGeoEntityMock = mock(GeoEntity.class);

    mTestSubject =
        new SendRemoteAddDynamicEntityRequestInteractor(Schedulers::trampoline, mRemoteSourceMock,
            dynamicLayersRepositoryMock, mGeoEntityMock);
  }

  @Test
  public void whenExecuted_shouldCallRemoteSourceWithEntity() throws Exception {
    //Act
    mTestSubject.execute();

    //Assert
    verify(mRemoteSourceMock).addEntity(any(), eq(mGeoEntityMock));
  }

  @Test
  public void shouldUseSingleExistingDynamicLayer() throws Exception {
    //Act
    mTestSubject.execute();

    //Assert
    verify(mRemoteSourceMock).addEntity(eq(mSingleDynamicLayer), any());
  }
}
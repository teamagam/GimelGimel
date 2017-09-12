package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SendRemoteRemoveDynamicEntityRequestInteractorTest extends BaseTest {

  private SendRemoteRemoveDynamicEntityRequestInteractor mTestSubject;
  private DynamicLayerRemoteSourceHandler mRemoteSourceMock;
  private DynamicEntity mDynamicEntity;
  private DynamicLayer mSingleDynamicLayer;

  @Before
  public void setUp() throws Exception {
    DynamicLayersRepository dynamicLayersRepositoryMock = mock(DynamicLayersRepository.class);
    List<DynamicEntity> entities = new ArrayList<>();
    mDynamicEntity = mock(DynamicEntity.class);
    GeoEntity geoMock = mock(GeoEntity.class);
    when(mDynamicEntity.getGeoEntity()).thenReturn(geoMock);
    when(geoMock.getId()).thenReturn("geo_entity_id");
    entities.add(mDynamicEntity);

    mSingleDynamicLayer =
        new DynamicLayer("dynamic_layer_id", "dynamic_layer_name", "", 0, entities);
    when(dynamicLayersRepositoryMock.getById(mSingleDynamicLayer.getId())).thenReturn(
        mSingleDynamicLayer);

    mRemoteSourceMock = spy(DynamicLayerRemoteSourceHandler.class);

    ThreadExecutor threadExecutor = Schedulers::trampoline;

    mTestSubject =
        new SendRemoteRemoveDynamicEntityRequestInteractor(threadExecutor, mRemoteSourceMock,
            dynamicLayersRepositoryMock, new RetryWithDelay(1, 1, threadExecutor),
            mSingleDynamicLayer.getId(), mDynamicEntity.getId());
  }

  @Test
  public void whenExecuted_shouldCallRemoteSourceWithEntity() throws Exception {
    //Act
    mTestSubject.execute();

    //Assert
    verify(mRemoteSourceMock).removeEntity(eq(mSingleDynamicLayer), eq(mDynamicEntity));
  }
}
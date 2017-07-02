package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessMessagesInteractorTest extends BaseTest {

  private static final String MESSAGE_ID = "messageId";
  private static final String SENDER_ID = "senderId";
  private static final String ENTITY_ID = "entityId";
  private static final String ALERT_ID = "alertId";
  private GeoEntity mEntity;
  private MessagesRepository mMessagesRepository;
  private Alert mAlert;

  @Before
  public void setUp() throws Exception {
    mEntity = mock(GeoEntity.class);
    when(mEntity.getId()).thenReturn(ENTITY_ID);
    mAlert = mock(Alert.class);
    when(mAlert.getId()).thenReturn(ALERT_ID);
    ChatMessage geoAlertMessage =
        new ChatMessage(MESSAGE_ID, SENDER_ID, new Date(), new GeoFeature(mEntity),
            new AlertFeature(mAlert));
    mMessagesRepository = mock(MessagesRepository.class);
    when(mMessagesRepository.getMessagesObservable()).thenReturn(Observable.just(geoAlertMessage));
  }

  @Test
  public void whenGeoMessageEmitted_itShouldBeAddedToRepoAndDisplayed() throws Exception {
    // Arrange
    GeoEntitiesRepository geoRepo = Mockito.spy(GeoEntitiesRepository.class);
    DisplayedEntitiesRepository displayedGeoRepo = Mockito.spy(DisplayedEntitiesRepository.class);

    // Act
    executeInteractor(geoRepo, displayedGeoRepo, mock(AlertsRepository.class),
        mock(ObjectMessageMapper.class), mock(ObjectMessageMapper.class));

    // Assert
    Mockito.verify(geoRepo).add(mEntity);
    Mockito.verify(displayedGeoRepo).show(mEntity);
  }

  @Test
  public void whenGeoMessageEmitted_shouldMapMessageToEntity() throws Exception {
    // Arrange
    ObjectMessageMapper entityMessageMapper = Mockito.spy(ObjectMessageMapper.class);

    // Act
    executeInteractor(mock(GeoEntitiesRepository.class), mock(DisplayedEntitiesRepository.class),
        mock(AlertsRepository.class), entityMessageMapper, mock(ObjectMessageMapper.class));

    // Assert
    Mockito.verify(entityMessageMapper).addMapping(MESSAGE_ID, ENTITY_ID);
  }

  @Test
  public void whenAlertMessageEmitted_itShouldBeAddedToRepo() throws Exception {
    // Arrange
    AlertsRepository alertsRepository = Mockito.spy(AlertsRepository.class);

    // Act
    executeInteractor(mock(GeoEntitiesRepository.class), mock(DisplayedEntitiesRepository.class),
        alertsRepository, mock(ObjectMessageMapper.class), mock(ObjectMessageMapper.class));

    // Assert
    Mockito.verify(alertsRepository).addAlert(mAlert);
  }

  @Test
  public void whenAlertMessageEmitted_shouldMapMessageToEntity() throws Exception {
    // Arrange
    ObjectMessageMapper alertMessageMapper = Mockito.spy(ObjectMessageMapper.class);

    // Act
    executeInteractor(mock(GeoEntitiesRepository.class), mock(DisplayedEntitiesRepository.class),
        mock(AlertsRepository.class), mock(ObjectMessageMapper.class), alertMessageMapper);

    // Assert
    Mockito.verify(alertMessageMapper).addMapping(MESSAGE_ID, ALERT_ID);
  }

  private void executeInteractor(GeoEntitiesRepository geoEntitiesRepository,
      DisplayedEntitiesRepository displayedEntitiesRepository,
      AlertsRepository alertsRepository,
      ObjectMessageMapper entityMessageMapper,
      ObjectMessageMapper alertMessageMapper) {
    new ProcessMessagesInteractor(Schedulers::trampoline, mMessagesRepository,
        geoEntitiesRepository, displayedEntitiesRepository, alertsRepository, entityMessageMapper,
        alertMessageMapper).execute();
  }
}
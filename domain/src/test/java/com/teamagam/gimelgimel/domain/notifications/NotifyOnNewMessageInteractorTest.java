package com.teamagam.gimelgimel.domain.notifications;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

public class NotifyOnNewMessageInteractorTest extends BaseTest {

  private PreferencesUtils mPreferencesUtils;
  private ChatMessage mChatMessage;
  private ChatMessage mAlertMessage;
  private MessagesRepository mMessagesRepository;
  private NotifyOnNewMessageInteractor.NotificationDisplayer mNotificationDisplayer;

  @Before
  public void setUp() throws Exception {
    mPreferencesUtils = mock(PreferencesUtils.class);

    mChatMessage = mock(ChatMessage.class);

    mAlertMessage = mock(ChatMessage.class);
    when(mAlertMessage.contains(AlertFeature.class)).thenReturn(true);

    mMessagesRepository = mock(MessagesRepository.class);
    when(mMessagesRepository.getMessagesObservable()).thenReturn(
        Observable.just(mChatMessage, mAlertMessage));

    mNotificationDisplayer = Mockito.spy(NotifyOnNewMessageInteractor.NotificationDisplayer.class);
  }

  @Test
  public void onRegularMode_shouldNotifyBothMessages() {
    // Arrange
    when(mPreferencesUtils.isOnlyAlertsMode()).thenReturn(false);

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mNotificationDisplayer).notifyNewMessage(mChatMessage);
    Mockito.verify(mNotificationDisplayer).notifyNewMessage(mAlertMessage);
  }

  @Test
  public void onStrictMode_shouldNotifyOnlyAlerts() {
    // Arrange
    when(mPreferencesUtils.isOnlyAlertsMode()).thenReturn(true);

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mNotificationDisplayer, never()).notifyNewMessage(mChatMessage);
    Mockito.verify(mNotificationDisplayer).notifyNewMessage(mAlertMessage);
  }

  private void executeInteractor() {
    new NotifyOnNewMessageInteractor(Schedulers::trampoline, Schedulers::trampoline,
        mPreferencesUtils, mMessagesRepository, mNotificationDisplayer).execute();
  }
}
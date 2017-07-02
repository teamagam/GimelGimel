package com.teamagam.gimelgimel.domain.notifications;

import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.ApplicationStatus;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.Date;
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
  private UnreadMessagesCountRepository mUnreadRepository;
  private ApplicationStatus mApplicationStatus;

  @Before
  public void setUp() throws Exception {
    mChatMessage = mock(ChatMessage.class);
    when(mChatMessage.getCreatedAt()).thenReturn(new Date());

    mAlertMessage = mock(ChatMessage.class);
    when(mAlertMessage.contains(AlertFeature.class)).thenReturn(true);
    when(mAlertMessage.getCreatedAt()).thenReturn(new Date());

    mPreferencesUtils = mock(PreferencesUtils.class);
    when(mPreferencesUtils.isSelf(mChatMessage.getSenderId())).thenReturn(false);

    mMessagesRepository = mock(MessagesRepository.class);
    when(mMessagesRepository.getMessagesObservable()).thenReturn(
        Observable.just(mChatMessage, mAlertMessage));

    mNotificationDisplayer = Mockito.spy(NotifyOnNewMessageInteractor.NotificationDisplayer.class);

    mUnreadRepository = mock(UnreadMessagesCountRepository.class);
    when(mUnreadRepository.getLastVisitTimestamp()).thenReturn(new Date(0));

    mApplicationStatus = mock(ApplicationStatus.class);
    when(mApplicationStatus.isAppOnForeground()).thenReturn(false);
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

  @Test
  public void whenMessageFromSelf_ShouldNotNotify() {
    // Arrange
    when(mPreferencesUtils.isSelf(mChatMessage.getSenderId())).thenReturn(true);

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mNotificationDisplayer, never()).notifyNewMessage(mChatMessage);
  }

  @Test
  public void whenMessageBeforeLastRead_ShouldNotNotify() {
    // Arrange
    when(mChatMessage.getCreatedAt()).thenReturn(new Date(0));
    when(mUnreadRepository.getLastVisitTimestamp()).thenReturn(new Date());

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mNotificationDisplayer, never()).notifyNewMessage(mChatMessage);
  }

  @Test
  public void whenApplicationOnBackground_ShouldNotNotify() {
    // Arrange
    when(mApplicationStatus.isAppOnForeground()).thenReturn(true);

    // Act
    executeInteractor();

    // Assert
    Mockito.verify(mNotificationDisplayer, never()).notifyNewMessage(mChatMessage);
  }

  private void executeInteractor() {
    new NotifyOnNewMessageInteractor(Schedulers::trampoline, Schedulers::trampoline,
        mPreferencesUtils, mMessagesRepository, mUnreadRepository, mApplicationStatus,
        mNotificationDisplayer).execute();
  }
}
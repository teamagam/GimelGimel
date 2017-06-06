package com.teamagam.gimelgimel.domain.alerts.repository;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import rx.Observable;

public interface AlertsRepository {

  void addAlert(ChatMessage alert);

  Observable<ChatMessage> getAlertsObservable();
}

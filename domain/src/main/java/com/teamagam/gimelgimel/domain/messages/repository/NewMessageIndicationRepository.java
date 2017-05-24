package com.teamagam.gimelgimel.domain.messages.repository;

import java.util.Date;

public interface NewMessageIndicationRepository {

  void set(Date indicationDate);

  Date get();
}
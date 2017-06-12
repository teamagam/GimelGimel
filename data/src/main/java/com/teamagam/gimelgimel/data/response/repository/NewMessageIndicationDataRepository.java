package com.teamagam.gimelgimel.data.response.repository;

import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;
import java.util.Date;
import javax.inject.Inject;

public class NewMessageIndicationDataRepository implements NewMessageIndicationRepository {

  private Date mIndicationDate;

  @Inject
  public NewMessageIndicationDataRepository() {
    mIndicationDate = new Date(0);
  }

  @Override
  public void set(Date indicationDate) {
    mIndicationDate = indicationDate;
  }

  @Override
  public Date get() {
    return mIndicationDate;
  }
}
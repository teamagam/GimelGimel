package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PolledMessagesProcessor implements IPolledMessagesProcessor {

  private static Logger sLogger =
      LoggerFactory.create(PolledMessagesProcessor.class.getSimpleName());

  private MessagesRepository mMessagesRepository;
  private VectorLayersRepository mVectorLayersRepository;
  private DynamicLayersRepository mDynamicLayersRepository;
  private UsersLocationRepository mUsersLocationRepository;
  private PreferencesUtils mPreferencesUtils;

  @Inject
  public PolledMessagesProcessor(MessagesRepository messagesRepository,
      VectorLayersRepository vectorLayersRepository,
      DynamicLayersRepository dynamicLayersRepository,
      UsersLocationRepository usersLocationRepository,
      PreferencesUtils preferencesUtils) {
    mMessagesRepository = messagesRepository;
    mDynamicLayersRepository = dynamicLayersRepository;
    mPreferencesUtils = preferencesUtils;
    mVectorLayersRepository = vectorLayersRepository;
    mUsersLocationRepository = usersLocationRepository;
  }

  @Override
  public void process(ChatMessage message) {
    if (message != null) {
      mMessagesRepository.putMessage(message);
    } else {
      throw new IllegalArgumentException("polledMessages cannot be null");
    }
  }

  @Override
  public void process(VectorLayer vectorLayer) {
    if (!mVectorLayersRepository.isOutdatedVectorLayer(vectorLayer)) {
      mVectorLayersRepository.put(vectorLayer);
    }
  }

  @Override
  public void process(DynamicLayer dynamicLayer) {
    mDynamicLayersRepository.put(dynamicLayer);
  }

  @Override
  public void process(UserLocation userLocation) {
    if (!mPreferencesUtils.isSelf(userLocation.getUser())) {
      mUsersLocationRepository.add(userLocation);
    }
  }
}
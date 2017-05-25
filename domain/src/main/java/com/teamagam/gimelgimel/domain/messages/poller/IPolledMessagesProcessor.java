package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.messages.entity.Message;
import java.util.Collection;

/**
 * Defines processing functionality for polled messages
 */
public interface IPolledMessagesProcessor {
  void process(Collection<Message> polledMessages);
}

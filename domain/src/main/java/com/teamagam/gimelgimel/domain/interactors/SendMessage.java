package com.teamagam.gimelgimel.domain.interactors;

import com.teamagam.gimelgimel.domain.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.entities.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import rx.Observable;

public class SendMessage extends BaseUseCase {


  private final MessagesRepository messagesRepository;
  private final Message message;

  public SendMessage(Message message, MessagesRepository messagesRepository, ThreadExecutor
          threadExecutor,
                     PostExecutionThread postExecutionThread) {
    super(threadExecutor, postExecutionThread);
    this.message = message;
    this.messagesRepository = messagesRepository;
  }

  @Override public Observable buildUseCaseObservable() {
    return this.messagesRepository.sendMessage(this.message)
            .doOnNext(this.messagesRepository::putMessage);
  }
}

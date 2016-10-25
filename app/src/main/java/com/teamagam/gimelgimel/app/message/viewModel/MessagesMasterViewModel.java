package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.common.DataChangedObserver;
import com.teamagam.gimelgimel.app.common.DataRandomAccessor;
import com.teamagam.gimelgimel.app.message.view.MessagesMasterFragment;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessagesRecyclerViewAdapter;
import com.teamagam.gimelgimel.app.model.ViewsModels.Message;
import com.teamagam.gimelgimel.app.model.ViewsModels.messages.DisplayMessage;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesModel;
import com.teamagam.gimelgimel.app.model.entities.messages.MessagesReadStatusModel;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.GetMessagesInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SyncMessagesInteractorFactory;

import javax.inject.Inject;

/**
 * Messages view-model for messages presentation use-case
 */
public class MessagesMasterViewModel extends SelectedMessageViewModel<MessagesMasterFragment>
        implements MessagesRecyclerViewAdapter.OnItemClickListener {

//    private MessagesModel mMessageModel;
//    private SelectedMessageModel mSelectedMessageModel;
//    private MessagesReadStatusModel mMessagesReadStatusModel;

    @Inject
    SyncMessagesInteractorFactory syncMessagesInteractorFactory;

    @Inject
    GetMessagesInteractorFactory getMessagesInteractorFactory;

    private SyncInteractor mSyncMessagesInteractor;

    public MessagesMasterViewModel() {
    }

    @Override
    public void start() {
        super.start();
        getMessagesInteractorFactory.create(new SimpleSubscriber<com.teamagam.gimelgimel.domain.messages.entity.Message>() {
            @Override
            public void onNext(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
                newMessageArrived(message);
            }
        }).execute();

        mSyncMessagesInteractor = syncMessagesInteractorFactory.create(
                new SimpleSubscriber<com.teamagam.gimelgimel.domain.messages.entity.Message>() {
                    @Override
                    public void onNext(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
                        newMessageArrived(message);
                    }
                }
        );
        mSyncMessagesInteractor.execute();
    }

    @Override
    public void stop() {
        super.stop();
        mSyncMessagesInteractor.unsubscribe();
    }

    private void newMessageArrived(com.teamagam.gimelgimel.domain.messages.entity.Message message) {
        mTransformer.transformToModel(message);
        //todo:complete
    }

    public void select(DisplayMessage displayMessage) {
        Message message = displayMessage.getMessage();
        markAsSelectedIfNotAlreadySelected(message);
        markAsReadIfNotAlreadyRead(message);
    }

    private void markAsReadIfNotAlreadyRead(Message message) {
        if (!mMessagesReadStatusModel.isRead(message)) {
            mMessagesReadStatusModel.markAsRead(message);
        }
    }

    private void markAsSelectedIfNotAlreadySelected(Message message) {
        if (mSelectedMessageModel.getSelected() != message) {
            mSelectedMessageModel.select(message);
        }
    }

    //    @Override
    public void onListItemInteraction(DisplayMessage message) {
        sLogger.userInteraction("Message [id=" + message.getMessage().getSenderId() + "] clicked");
//        mViewModel.select(message);
    }


    public DisplayedMessagesRandomAccessor getDisplayedMessagesRandomAccessor() {
        return new DisplayedMessagesRandomAccessor() {
            @Override
            public int size() {
                return mMessageModel.size();
            }

            @Override
            public DisplayMessage get(int index) {
                Message message = mMessageModel.get(index);
                return createDisplayMessage(message);
            }

            private DisplayMessage createDisplayMessage(Message message) {
                boolean isRead = isRead(message);
                boolean isSelected = isSelected(message);
                return new DisplayMessage.DisplayMessageBuilder().setMessage(message).setIsSelected(
                        isSelected).setIsRead(isRead).build();
            }

            private boolean isRead(Message message) {
                return mMessagesReadStatusModel.isRead(message);
            }

            private boolean isSelected(Message message) {
                return mSelectedMessageModel.getSelected() == message;
            }
        };
    }

    public interface DisplayedMessagesRandomAccessor extends DataRandomAccessor<DisplayMessage> {
    }

}

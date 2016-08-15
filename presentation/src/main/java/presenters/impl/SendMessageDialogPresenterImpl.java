package presenters.impl;

import com.gimelgimel.domain.repository.MessagesRepository;

import presenters.SendMessageDialogPresenter;
import presenters.base.AbstractPresenter;

public class SendMessageDialogPresenterImpl extends AbstractPresenter implements SendMessageDialogPresenter {

    View mView;
    MessagesRepository mMessagesRepository;

    public SendMessageDialogPresenterImpl(Executor executor, MainThread mainThread,
                                          View view, MessagesRepository messagesRepository) {
        super(executor, mainThread);

        mView = view;
        mMessagesRepository = messagesRepository;
    }

    @Override
    public void resume() {
        mView.showProgress();
    }

    @Override
    public void pause() {
        mView.hideProgress();
    }

    @Override
    public void stop() {
        mView.hideProgress();
    }

    @Override
    public void destroy() {
        mView.hideProgress();
    }

    @Override
    public void onError(String message) {
        mView.hideProgress();
        mView.showError(message);
    }
}

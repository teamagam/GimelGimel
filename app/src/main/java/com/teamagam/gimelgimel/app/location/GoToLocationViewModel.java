package com.teamagam.gimelgimel.app.location;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.view.LongLatPicker;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;

@AutoFactory
public class GoToLocationViewModel {

    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final LongLatPicker mLongLatPicker;
    private GoToLocationDialogFragment mView;

    public GoToLocationViewModel(
            @Provided GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            LongLatPicker longLatPicker,
            GoToLocationDialogFragment fragment) {
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mLongLatPicker = longLatPicker;
        mView = fragment;
    }

    public void start() {
        mLongLatPicker.setOnValidStateChangedListener(
                new LongLatPicker.OnValidStateChangedListener() {
                    @Override
                    public void onValid() {
                        setPositiveButtonEnabled(true);
                    }

                    @Override
                    public void onInvalid() {
                        setPositiveButtonEnabled(false);
                    }
                });
        setPositiveButtonEnabled(mLongLatPicker.hasPoint());
    }

    public void stop() {
        mLongLatPicker.setOnValidStateChangedListener(null);
    }

    public void onPositiveButtonClicked() {
        mGoToLocationMapInteractorFactory.create(mLongLatPicker.getPoint()).execute();
    }

    private void setPositiveButtonEnabled(boolean enabled) {
        mView.setPositiveButtonEnabled(enabled);
    }
}
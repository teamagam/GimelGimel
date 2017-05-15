package com.teamagam.gimelgimel.app.location;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.view.LongLatPicker;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import static com.teamagam.gimelgimel.data.config.Constants.USE_UTM_PREF_KEY;

@AutoFactory
public class GoToLocationViewModel {

    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final UserPreferencesRepository mUserPreferencesRepo;
    private final LongLatPicker mLongLatPicker;
    private GoToLocationDialogFragment mView;

    public GoToLocationViewModel(
            @Provided GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            @Provided UserPreferencesRepository userPreferencesRepo,
            LongLatPicker longLatPicker,
            GoToLocationDialogFragment fragment) {
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mUserPreferencesRepo = userPreferencesRepo;
        mLongLatPicker = longLatPicker;
        mLongLatPicker.setCoordinateSystem(shouldUseUtm());
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

    private boolean shouldUseUtm() {
        return mUserPreferencesRepo.getBoolean(USE_UTM_PREF_KEY);
    }

    private void setPositiveButtonEnabled(boolean enabled) {
        mView.setPositiveButtonEnabled(enabled);
    }
}
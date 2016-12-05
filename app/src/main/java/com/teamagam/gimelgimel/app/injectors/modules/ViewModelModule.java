package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Activity;
import android.content.Context;

import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.viewModel.GeoMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.ImageMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.TextMessageDetailViewModel;
import com.teamagam.gimelgimel.app.message.viewModel.adapter.MessageAppMapper;
import com.teamagam.gimelgimel.domain.map.DrawMessageOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.DisplaySelectedMessageInteractorFactory;

import dagger.Module;
import dagger.Provides;

@Module
@PerActivity
public class ViewModelModule {

    @Provides
    @PerActivity
    GeoMessageDetailViewModel provideGeoMessageDetailViewModel(Context context,
                                                               DisplaySelectedMessageInteractorFactory displayInteractorFactory,
                                                               GoToLocationMapInteractorFactory gotoLocationInteractorFactory,
                                                               DrawMessageOnMapInteractorFactory drawMessageInteractorFactory,
                                                               GeoEntityTransformer geoEntityTransformer,
                                                               MessageAppMapper messageAppMapper) {
        return new GeoMessageDetailViewModel(
                context, displayInteractorFactory,
                gotoLocationInteractorFactory, drawMessageInteractorFactory,
                geoEntityTransformer, messageAppMapper);
    }

    @Provides
    @PerActivity
    TextMessageDetailViewModel provideTextMessageDetailViewModel(Context context,
                                                                 DisplaySelectedMessageInteractorFactory displayInteractorFactory,
                                                                 MessageAppMapper messageAppMapper) {
        return new TextMessageDetailViewModel(context, displayInteractorFactory, messageAppMapper);
    }

    @Provides
    @PerActivity
    ImageMessageDetailViewModel provideImageMessageDetailViewModel(Context context,
                                                                   DisplaySelectedMessageInteractorFactory displayInteractorFactory,
                                                                   GoToLocationMapInteractorFactory gotoLocationInteractorFactory,
                                                                   DrawMessageOnMapInteractorFactory drawMessageInteractorFactory,
                                                                   Navigator navigator,
                                                                   Activity activity,
                                                                   GeoEntityTransformer geoEntityTransformer,
                                                                   MessageAppMapper messageAppMapper) {
        return new ImageMessageDetailViewModel(
                context, displayInteractorFactory,
                gotoLocationInteractorFactory, drawMessageInteractorFactory,
                navigator, activity,
                geoEntityTransformer, messageAppMapper);
    }
}

package com.teamagam.gimelgimel.app.injectors.components;

import android.content.Context;

import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.PreferencesModule;
import com.teamagam.gimelgimel.app.utils.SecuredPreferenceUtil;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(
        modules = {
                ApplicationModule.class,
                PreferencesModule.class
        })
public interface ApplicationComponent {
//  void inject(BaseActivity baseActivity);

  //Exposed to sub-graphs.
  Context context();
  ThreadExecutor threadExecutor();
  PostExecutionThread postExecutionThread();
  MessagesRepository messagesRepository();
  SecuredPreferenceUtil sharedPreferences();
}

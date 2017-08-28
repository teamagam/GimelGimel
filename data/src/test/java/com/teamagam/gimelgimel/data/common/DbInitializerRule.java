package com.teamagam.gimelgimel.data.common;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.robolectric.RuntimeEnvironment;

public class DbInitializerRule implements TestRule {

  private final DbInitializer mInitializer = new DbInitializer();
  private AppDatabase mDb;

  @Override
  public Statement apply(Statement statement, Description description) {
    return mInitializer.apply(statement, description);
  }

  public AppDatabase getDb() {
    return mDb;
  }

  private class DbInitializer extends ExternalResource {

    @Override
    protected void before() throws Throwable {
      initializeInMemoryDb();
    }

    @Override
    protected void after() {
      mDb.close();
    }

    private void initializeInMemoryDb() {
      Context context = RuntimeEnvironment.application.getApplicationContext();
      mDb =
          Room.inMemoryDatabaseBuilder(context, AppDatabase.class).allowMainThreadQueries().build();
    }
  }
}

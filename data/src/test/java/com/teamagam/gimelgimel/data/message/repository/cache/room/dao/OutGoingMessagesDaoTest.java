package com.teamagam.gimelgimel.data.message.repository.cache.room.dao;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import com.teamagam.gimelgimel.data.message.repository.cache.room.AppDatabase;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class OutGoingMessagesDaoTest extends BaseTest {

  @Rule
  public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
  private AppDatabase mDb;
  private OutGoingMessagesDao mDao;
  private MessagesEntityMapper mMapper;

  @Before
  public void setup() {

  }

  @Test
  public void getMessagesByOutGoingId() throws Exception {

  }

  @Test
  public void insertMessage() throws Exception {

  }

  @Test

  public void deleteTopMessage() throws Exception {

  }

  @Test
  public void nukeTable() throws Exception {

  }
}
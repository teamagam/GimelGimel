package com.teamagam.gimelgimel.app.integration.interactors;

import com.teamagam.gimelgimel.data.map.repository.VectorLayersVisibilityDataRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.hamcrest.core.Is.is;

public class DisplayVectorLayerInteractorTest extends BaseTest {

    private DisplayVectorLayersInteractor mSubject;
    private VectorLayersVisibilityDataRepository mVectorLayersVisibilityRepository;
    private VisibilityStatusTestDisplayer mDisplayer;

    private Scheduler createTestScheduler() {
        return Schedulers.immediate();
    }

    @Before
    public void setUp() throws Exception {
        mVectorLayersVisibilityRepository = new VectorLayersVisibilityDataRepository();
        mDisplayer = new VisibilityStatusTestDisplayer();
        mSubject = new DisplayVectorLayersInteractor(new ThreadExecutor() {
            @Override
            public Scheduler getScheduler() {
                return createTestScheduler();
            }
        }, new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return createTestScheduler();
            }
        },
                mVectorLayersVisibilityRepository, mDisplayer);
    }

    @Test
    public void executeThenSetVisibleVL_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);

        //Act
        mSubject.execute();
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, true));

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void executeThenSetInvisibleVL_VLShouldBeInvisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);

        //Act
        mSubject.execute();
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, false));

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
    }

    @Test
    public void setVisibleVLThenExecute_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);

        //Act
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, true));
        mSubject.execute();

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void setVisibleThenSetInvisible_VLShouldBeInvisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);

        //Act
        mSubject.execute();
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, true));
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, false));

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
    }

    @Test
    public void setVisibleThenSetVisibleAgain_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);

        //Act
        mSubject.execute();
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, true));
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl, true));

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void setFirstVisibleThenExecuteThenSetSecondVisible_BothShouldBeVisible() throws
            Exception {
        //Arrange
        VectorLayer vl1 = new VectorLayer("1", "name1", null);
        VectorLayer vl2 = new VectorLayer("2", "name2", null);

        //Act
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl1, true));
        mSubject.execute();
        mVectorLayersVisibilityRepository.changeVectorLayerVisibility(new
                VectorLayerVisibilityChange(vl2, false));

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    private static class VisibilityStatusTestDisplayer implements DisplayVectorLayersInteractor.Displayer {
        private Map<String, Boolean> mVisibilityStatus;

        public VisibilityStatusTestDisplayer() {
            mVisibilityStatus = new HashMap<>();
        }

        @Override
        public void setVisibility(VectorLayer vectorLayer, boolean visibility) {
            mVisibilityStatus.put(vectorLayer.getId(), visibility);
        }

        public boolean getVisibility(String id) {
            if (mVisibilityStatus.containsKey(id)) {
                return mVisibilityStatus.get(id);
            } else {
                throw new RuntimeException(String.format("VectorLayer with id %s is unknown", id));
            }
        }
    }
}

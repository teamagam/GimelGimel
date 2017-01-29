package com.teamagam.gimelgimel.app.integration.interactors;

import com.teamagam.gimelgimel.data.map.repository.VectorLayersDataRepository;
import com.teamagam.gimelgimel.data.map.repository.VectorLayersVisibilityDataRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.sharedTest.BaseTest;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.SetVectorLayerVisibilityInteractor;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import rx.Scheduler;
import rx.schedulers.Schedulers;

import static org.hamcrest.core.Is.is;

public class DisplayVectorLayerInteractorTest extends BaseTest {

    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private VectorLayersRepository mVectorLayersRepository;
    private VisibilityStatusTestDisplayer mDisplayer;
    private VectorLayersVisibilityDataRepository mVectorLayersVisibilityRepository;

    @Before
    public void setUp() throws Exception {
        mVectorLayersRepository = new VectorLayersDataRepository();
        mDisplayer = new VisibilityStatusTestDisplayer();
        mVectorLayersVisibilityRepository = new VectorLayersVisibilityDataRepository();
        mDisplayVectorLayersInteractor = new DisplayVectorLayersInteractor(new ThreadExecutor() {
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
                mVectorLayersRepository, mVectorLayersVisibilityRepository, mDisplayer);
    }

    @Test
    public void executeThenSetVisibleVL_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);
        mVectorLayersRepository.add(vl);

        //Act
        mDisplayVectorLayersInteractor.execute();
        executeSetVectorLayerVisibilityInteractor(vl.getId(), true);

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void executeThenSetInvisibleVL_VLShouldBeInvisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);
        mVectorLayersRepository.add(vl);

        //Act
        mDisplayVectorLayersInteractor.execute();
        executeSetVectorLayerVisibilityInteractor(vl.getId(), false);

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
    }

    @Test
    public void setVisibleVLThenExecute_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);
        mVectorLayersRepository.add(vl);

        //Act
        executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
        mDisplayVectorLayersInteractor.execute();

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void setVisibleThenSetInvisible_VLShouldBeInvisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);
        mVectorLayersRepository.add(vl);

        //Act
        mDisplayVectorLayersInteractor.execute();
        executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
        executeSetVectorLayerVisibilityInteractor(vl.getId(), false);

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(false));
    }

    @Test
    public void setVisibleThenSetVisibleAgain_VLShouldBeVisible() throws Exception {
        //Arrange
        VectorLayer vl = new VectorLayer("1", "name1", null);
        mVectorLayersRepository.add(vl);

        //Act
        mDisplayVectorLayersInteractor.execute();
        executeSetVectorLayerVisibilityInteractor(vl.getId(), true);
        executeSetVectorLayerVisibilityInteractor(vl.getId(), true);

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
    }

    @Test
    public void setFirstVisibleThenExecuteThenSetSecondVisible_BothShouldBeVisible() throws
            Exception {
        //Arrange
        VectorLayer vl1 = new VectorLayer("1", "name1", null);
        VectorLayer vl2 = new VectorLayer("2", "name2", null);
        mVectorLayersRepository.add(vl1);
        mVectorLayersRepository.add(vl2);

        //Act
        executeSetVectorLayerVisibilityInteractor(vl1.getId(), true);
        mDisplayVectorLayersInteractor.execute();
        executeSetVectorLayerVisibilityInteractor(vl2.getId(), true);

        //Assert
        Assert.assertThat(mDisplayer.getVisibility("1"), is(true));
        Assert.assertThat(mDisplayer.getVisibility("2"), is(true));
    }

    private void executeSetVectorLayerVisibilityInteractor(String id, boolean isVisible) {
        new SetVectorLayerVisibilityInteractor(new ThreadExecutor() {
            @Override
            public Scheduler getScheduler() {
                return createTestScheduler();
            }
        }, mVectorLayersVisibilityRepository, id, isVisible).execute();
    }

    private Scheduler createTestScheduler() {
        return Schedulers.immediate();
    }

    private static class VisibilityStatusTestDisplayer implements DisplayVectorLayersInteractor.Displayer {
        private Map<String, Boolean> mVisibilityStatus;

        public VisibilityStatusTestDisplayer() {
            mVisibilityStatus = new TreeMap<>();
        }

        public boolean getVisibility(String id) {
            if (mVisibilityStatus.containsKey(id)) {
                return mVisibilityStatus.get(id);
            } else {
                throw new RuntimeException(
                        String.format("VectorLayer with id %s was never displayed", id));
            }
        }

        @Override
        public void displayShown(VectorLayer vectorLayer) {
            mVisibilityStatus.put(vectorLayer.getId(), true);
        }

        @Override
        public void displayHidden(VectorLayer vectorLayer) {
            mVisibilityStatus.put(vectorLayer.getId(), false);
        }
    }
}
package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.view.MapEntityDetailsFragment;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayKmlEntityInfoInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;

import javax.inject.Inject;

public class MapEntityDetailsViewModel extends BaseViewModel<MapEntityDetailsFragment> {

    @Inject
    DisplayKmlEntityInfoInteractorFactory mDisplayKmlEntityInfoInteractorFactory;
    @Inject
    VectorLayersRepository mVectorLayersRepository;

    private DisplayKmlEntityInfoInteractor mDisplayKmlEntityInfoInteractor;

    private String mVectorLayerName;
    private String mEntityName;
    private String mEntityDescription;

    @Inject
    public MapEntityDetailsViewModel() {
    }

    @Override
    public void init() {
        super.init();
        mDisplayKmlEntityInfoInteractor = mDisplayKmlEntityInfoInteractorFactory.create(
                new DisplayKmlEntityInfoInteractor.Displayer() {
                    @Override
                    public void display(KmlEntityInfo kmlEntityInfo) {
                        mEntityName = kmlEntityInfo.getName();
                        String id = kmlEntityInfo.getVectorLayerId();
                        mVectorLayerName = mVectorLayersRepository.get(id).getName();
                        mEntityDescription = kmlEntityInfo.getDescription();
                        notifyChange();
                    }

                    @Override
                    public void hide() {

                    }
                });
    }

    @Override
    public void start() {
        super.start();
        mDisplayKmlEntityInfoInteractor.execute();
    }

    @Override
    public void stop() {
        if (mDisplayKmlEntityInfoInteractor != null) {
            mDisplayKmlEntityInfoInteractor.unsubscribe();
        }
        super.stop();
    }

    public String getVectorLayerTitle() {
        return getString(R.string.vector_layer_title_prefix) + mVectorLayerName;
    }

    private String getString(int resId) {
        return mView.getResources().getString(resId);
    }

    public String getEntityName() {
        return mEntityName;
    }

    public String getEntityDescription() {
        return mEntityDescription;
    }
}

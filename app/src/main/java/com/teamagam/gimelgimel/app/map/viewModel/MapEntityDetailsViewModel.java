package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.map.view.MapEntityDetailsFragment;

import javax.inject.Inject;

public class MapEntityDetailsViewModel extends BaseViewModel<MapEntityDetailsFragment> {

    private String mEntityName;
    private String mEntityDescription;

    @Inject
    public MapEntityDetailsViewModel() {
        mEntityName = "Entity Name";
        mEntityDescription = "Entity Description";
    }

    public String getEntityName() {
        return mEntityName;
    }

    public String getEntityDescription() {
        return mEntityDescription;
    }
}

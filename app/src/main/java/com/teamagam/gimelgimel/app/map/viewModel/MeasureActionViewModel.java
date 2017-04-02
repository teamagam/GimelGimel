package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class MeasureActionViewModel extends BaseMapViewModel {

    protected MeasureActionViewModel(
            @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
            @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            @Provided DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
            GGMapView ggMapView) {
        super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
                displayIntermediateRastersInteractorFactory, ggMapView);
    }


    public void onPlusClicked() {
        //no-op
    }

    public String getBottomSheetText() {
        return "My Text";
    }

    public void setBottomSheetText(String text){

    }
}
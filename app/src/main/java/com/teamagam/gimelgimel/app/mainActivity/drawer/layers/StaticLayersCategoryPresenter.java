package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.OnVectorLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;

@AutoFactory
public class StaticLayersCategoryPresenter extends VectorLayersCategoryPresenter {

  private final Context mContext;

  protected StaticLayersCategoryPresenter(@Provided
      OnVectorLayerListingClickInteractorFactory onVectorLayerListingClickInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided Context context,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(onVectorLayerListingClickInteractorFactory, displayVectorLayersInteractorFactory,
        layersNodeDisplayer);
    mContext = context;
  }

  @Override
  protected boolean shouldDisplay(VectorLayerPresentation vlp) {
    return vlp.getCategory() == VectorLayer.Category.SECOND;
  }

  @Override
  protected String getCategoryTitle() {
    return mContext.getString(R.string.drawer_layers_category_name_static_layers);
  }
}

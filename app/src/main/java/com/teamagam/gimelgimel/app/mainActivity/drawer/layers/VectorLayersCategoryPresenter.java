package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.OnVectorLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import io.reactivex.Observable;

abstract class VectorLayersCategoryPresenter
    extends DrawerCategoryPresenter<VectorLayerPresentation> {

  private final static AppLogger sLogger = AppLoggerFactory.create();

  private final OnVectorLayerListingClickInteractorFactory
      mOnVectorLayerListingClickInteractorFactory;
  private final VectorLayerNodeSelectionDisplayer mVectorLayerNodeSelectionDisplayer;
  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final SubjectRepository<VectorLayerPresentation> mSubject;
  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;

  protected VectorLayersCategoryPresenter(OnVectorLayerListingClickInteractorFactory onVectorLayerListingClickInteractorFactory,
      DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(layersNodeDisplayer);
    mVectorLayerNodeSelectionDisplayer = new VectorLayerNodeSelectionDisplayer(layersNodeDisplayer);
    mOnVectorLayerListingClickInteractorFactory = onVectorLayerListingClickInteractorFactory;
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void start() {
    mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(vlp -> {
      if (shouldDisplay(vlp)) {
        mSubject.add(vlp);
      }
    });
    mDisplayVectorLayersInteractor.execute();
  }

  @Override
  public void stop() {
    mDisplayVectorLayersInteractor.unsubscribe();
  }

  protected abstract boolean shouldDisplay(VectorLayerPresentation vlp);

  @Override
  protected abstract String getCategoryTitle();

  @Override
  protected Observable<VectorLayerPresentation> getItemObservable() {
    return mSubject.getObservable();
  }

  @Override
  protected NodeSelectionDisplayer<VectorLayerPresentation> getNodeDisplayer() {
    return mVectorLayerNodeSelectionDisplayer;
  }

  private class VectorLayerNodeSelectionDisplayer
      extends NodeSelectionDisplayer<VectorLayerPresentation> {

    public VectorLayerNodeSelectionDisplayer(LayersNodeDisplayer layersNodeDisplayer) {
      super(layersNodeDisplayer);
    }

    @Override
    protected LayersNodeDisplayer.Node createNode(VectorLayerPresentation vlp,
        LayersNodeDisplayer.Node parentNode) {
      return new LayersNodeDisplayer.NodeBuilder(vlp.getName()).setParentId(parentNode.getId())
          .setIsSelected(vlp.isShown())
          .setOnListingClickListener(view -> onVectorLayerClicked(vlp))
          .createNode();
    }

    @Override
    protected boolean isSelected(VectorLayerPresentation item) {
      return item.isShown();
    }

    private void onVectorLayerClicked(VectorLayerPresentation vlp) {
      sLogger.userInteraction("Click on vl " + vlp.getName());
      mOnVectorLayerListingClickInteractorFactory.create(vlp).execute();
    }
  }
}

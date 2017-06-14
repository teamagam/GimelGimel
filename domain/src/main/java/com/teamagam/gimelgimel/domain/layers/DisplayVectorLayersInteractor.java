package com.teamagam.gimelgimel.domain.layers;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import java.net.URI;
import java.util.Set;
import java.util.TreeSet;

@AutoFactory
public class DisplayVectorLayersInteractor extends BaseSingleDisplayInteractor {

  private final VectorLayersRepository mVectorLayersRepository;
  private final VectorLayersVisibilityRepository mVectorLayersVisibilityRepository;
  private final LayersLocalCache mLayersLocalCache;
  private final Set<String> mDisplayedVLs;
  private final Displayer mDisplayer;

  public DisplayVectorLayersInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided PostExecutionThread postExecutionThread,
      @Provided VectorLayersRepository vectorLayersRepository,
      @Provided VectorLayersVisibilityRepository vectorLayersVisibilityRepository,
      @Provided LayersLocalCache layersLocalCache,
      Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mVectorLayersRepository = vectorLayersRepository;
    mVectorLayersVisibilityRepository = vectorLayersVisibilityRepository;
    mLayersLocalCache = layersLocalCache;
    mDisplayer = displayer;
    mDisplayedVLs = new TreeSet<>();
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    return factory.create(mVectorLayersVisibilityRepository.getChangesObservable(),
        vectorLayerVisibilityChangeObservable -> vectorLayerVisibilityChangeObservable.map(
            this::createVectorLayerPresentation), this::display);
  }

  private VectorLayerPresentation createVectorLayerPresentation(VectorLayerVisibilityChange visibilityChange) {
    VectorLayer vl = mVectorLayersRepository.get(visibilityChange.getVectorLayerId());
    URI cachedURI = mLayersLocalCache.getCachedURI(vl);
    if (visibilityChange.isVisible()) {
      return VectorLayerPresentation.createShown(vl, cachedURI);
    }
    return VectorLayerPresentation.createHidden(vl, cachedURI);
  }

  private void display(VectorLayerPresentation vlp) {
    if (isUpdatingAlreadyShown(vlp)) {
      hidePreviousLayer(vlp);
    }
    mDisplayer.display(vlp);
    updateDisplayStatus(vlp);
  }

  private boolean isUpdatingAlreadyShown(VectorLayerPresentation vlp) {
    return vlp.isShown() && mDisplayedVLs.contains(vlp.getId());
  }

  private void hidePreviousLayer(VectorLayerPresentation vlp) {
    mDisplayer.display(VectorLayerPresentation.createHidden(vlp, vlp.getLocalURI()));
  }

  private void updateDisplayStatus(VectorLayerPresentation vlp) {
    String id = vlp.getId();
    if (vlp.isShown()) {
      mDisplayedVLs.add(id);
    } else {
      mDisplayedVLs.remove(id);
    }
  }

  public interface Displayer {
    void display(VectorLayerPresentation vectorLayerPresentation);
  }
}

package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.phase.PhaseLayer;
import com.teamagam.gimelgimel.domain.phase.visibility.DisplayPhaseLayersInteractor;
import com.teamagam.gimelgimel.domain.phase.visibility.DisplayPhaseLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.visibility.OnPhaseLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerPresentation;
import io.reactivex.Observable;

@AutoFactory
public class PhaseCategoryPresenter extends DrawerCategoryPresenter<PhaseLayerPresentation> {

  private final DisplayPhaseLayersInteractorFactory mDisplayPhaseLayersInteractorFactory;
  private final OnPhaseLayerListingClickInteractorFactory
      mOnPhaseLayerListingClickInteractorFactory;
  private final Context mContext;
  private final Navigator mNavigator;
  private final PhaseLayerNodeSelectionDisplayer mPhaseLayerNodeSelectionDisplayer;
  private final SubjectRepository<PhaseLayerPresentation> mSubject;
  private DisplayPhaseLayersInteractor mDisplayPhaseLayersInteractor;

  protected PhaseCategoryPresenter(
      @Provided DisplayPhaseLayersInteractorFactory displayPhaseLayersInteractorFactory,
      @Provided OnPhaseLayerListingClickInteractorFactory onPhaseLayerListingClickInteractorFactory,
      @Provided Context context, @Provided Navigator navigator,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(layersNodeDisplayer);
    mDisplayPhaseLayersInteractorFactory = displayPhaseLayersInteractorFactory;
    mContext = context;
    mNavigator = navigator;
    mPhaseLayerNodeSelectionDisplayer = new PhaseLayerNodeSelectionDisplayer(layersNodeDisplayer);
    mOnPhaseLayerListingClickInteractorFactory = onPhaseLayerListingClickInteractorFactory;
    mSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void start() {
    mDisplayPhaseLayersInteractor = mDisplayPhaseLayersInteractorFactory.create(mSubject::add);
    mDisplayPhaseLayersInteractor.execute();
  }

  @Override
  public void stop() {
    mDisplayPhaseLayersInteractor.unsubscribe();
  }

  @Override
  protected String getCategoryTitle() {
    return mContext.getString(R.string.drawer_layers_category_name_phase_layers);
  }

  @Override
  protected Observable<PhaseLayerPresentation> getItemObservable() {
    return mSubject.getObservable();
  }

  @Override
  protected NodeSelectionDisplayer<PhaseLayerPresentation> getNodeDisplayer() {
    return mPhaseLayerNodeSelectionDisplayer;
  }

  private class PhaseLayerNodeSelectionDisplayer
      extends NodeSelectionDisplayer<PhaseLayerPresentation> {
    public PhaseLayerNodeSelectionDisplayer(LayersNodeDisplayer layersNodeDisplayer) {
      super(layersNodeDisplayer);
    }

    @Override
    protected LayersNodeDisplayer.Node createNode(PhaseLayerPresentation phaseLayer,
        LayersNodeDisplayer.Node parentNode) {
      return new LayersNodeDisplayer.NodeBuilder(phaseLayer.getName()).setParentId(
          parentNode.getId())
          .setIsSelected(phaseLayer.isShown())
          .setOnListingClickListener((v) -> onPhaseLayerListingClick(phaseLayer))
          .setIcon(getWatchIcon())
          .setOnIconClickListener((v) -> onWatchPhaseLayerClicked(phaseLayer))
          .createNode();
    }

    @Override
    protected boolean isSelected(PhaseLayerPresentation item) {
      return item.isShown();
    }

    private void onPhaseLayerListingClick(PhaseLayer phaseLayer) {
      mOnPhaseLayerListingClickInteractorFactory.create(phaseLayer.getId()).execute();
    }

    private Drawable getWatchIcon() {
      Drawable d = ContextCompat.getDrawable(mContext, android.R.drawable.ic_dialog_info);
      DrawableCompat.setTint(d, ContextCompat.getColor(mContext, R.color.drawer_layers_edit));
      return d;
    }

    private void onWatchPhaseLayerClicked(PhaseLayerPresentation phaseLayer) {
      mNavigator.openPhaseAction(phaseLayer);
    }
  }
}

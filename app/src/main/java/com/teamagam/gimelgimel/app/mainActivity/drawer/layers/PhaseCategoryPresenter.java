package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.phase.visibility.DisplayPhaseLayerInteractor;
import com.teamagam.gimelgimel.domain.phase.visibility.DisplayPhaseLayerInteractorFactory;
import com.teamagam.gimelgimel.domain.phase.visibility.PhaseLayerPresentation;
import io.reactivex.Observable;

@AutoFactory
public class PhaseCategoryPresenter extends DrawerCategoryPresenter<PhaseLayerPresentation> {

  private final DisplayPhaseLayerInteractorFactory mDisplayPhaseLayerInteractorFactory;
  private final Context mContext;
  private final PhaseLayerNodeSelectionDisplayer mPhaseLayerNodeSelectionDisplayer;
  private final SubjectRepository<PhaseLayerPresentation> mSubject;
  private DisplayPhaseLayerInteractor mDisplayPhaseLayerInteractor;

  protected PhaseCategoryPresenter(
      @Provided DisplayPhaseLayerInteractorFactory displayPhaseLayerInteractorFactory,
      @Provided Context context,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(layersNodeDisplayer);
    mDisplayPhaseLayerInteractorFactory = displayPhaseLayerInteractorFactory;
    mContext = context;
    mPhaseLayerNodeSelectionDisplayer = new PhaseLayerNodeSelectionDisplayer(layersNodeDisplayer);
    mSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void start() {
    mDisplayPhaseLayerInteractor = mDisplayPhaseLayerInteractorFactory.create(mSubject::add);
    mDisplayPhaseLayerInteractor.execute();
  }

  @Override
  public void stop() {
    mDisplayPhaseLayerInteractor.unsubscribe();
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

  private static class PhaseLayerNodeSelectionDisplayer
      extends NodeSelectionDisplayer<PhaseLayerPresentation> {
    public PhaseLayerNodeSelectionDisplayer(LayersNodeDisplayer layersNodeDisplayer) {
      super(layersNodeDisplayer);
    }

    @Override
    protected LayersNodeDisplayer.Node createNode(PhaseLayerPresentation item,
        LayersNodeDisplayer.Node parentNode) {
      return new LayersNodeDisplayer.NodeBuilder(item.getName()).setParentId(parentNode.getId())
          .setIsSelected(item.isShown())
          .createNode();
    }

    @Override
    protected boolean isSelected(PhaseLayerPresentation item) {
      return item.isShown();
    }
  }
}

package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import com.teamagam.gimelgimel.domain.rasters.OnRasterListingClickedInteractorFactory;
import io.reactivex.Observable;

@AutoFactory
public class RasterCategoryPresenter
    extends DrawerCategoryPresenter<IntermediateRasterPresentation> {

  private final static AppLogger sLogger = AppLoggerFactory.create();

  private final Context mContext;
  private final RasterNodeSelectionDisplayer mRasterNodeSelectionDisplayer;
  private final OnRasterListingClickedInteractorFactory mOnRasterListingClickedInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;
  private final SubjectRepository<IntermediateRasterPresentation> mSubject;
  private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;

  protected RasterCategoryPresenter(@Provided Context context,
      @Provided OnRasterListingClickedInteractorFactory onRasterListingClickedInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(layersNodeDisplayer);
    mContext = context;
    mOnRasterListingClickedInteractorFactory = onRasterListingClickedInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mRasterNodeSelectionDisplayer = new RasterNodeSelectionDisplayer(layersNodeDisplayer);
    mSubject = SubjectRepository.createSimpleSubject();
  }

  @Override
  public void start() {
    mDisplayIntermediateRastersInteractor =
        mDisplayIntermediateRastersInteractorFactory.create(mSubject::add);
    mDisplayIntermediateRastersInteractor.execute();
  }

  @Override
  public void stop() {
    mDisplayIntermediateRastersInteractor.unsubscribe();
  }

  @Override
  protected String getCategoryTitle() {
    return mContext.getString(R.string.drawer_layers_category_name_rasters);
  }

  @Override
  protected Observable<IntermediateRasterPresentation> getItemObservable() {
    return mSubject.getObservable();
  }

  @Override
  protected NodeSelectionDisplayer<IntermediateRasterPresentation> getNodeDisplayer() {
    return mRasterNodeSelectionDisplayer;
  }

  private class RasterNodeSelectionDisplayer
      extends NodeSelectionDisplayer<IntermediateRasterPresentation> {

    public RasterNodeSelectionDisplayer(LayersNodeDisplayer layersNodeDisplayer) {
      super(layersNodeDisplayer);
    }

    @Override
    protected LayersNodeDisplayer.Node createNode(IntermediateRasterPresentation irp,
        LayersNodeDisplayer.Node parentNode) {
      return new LayersNodeDisplayer.NodeBuilder(irp.getName()).setParentId(parentNode.getId())
          .setIsSelected(irp.isShown())
          .setOnListingClickListener(view -> onRasterClicked(irp))
          .createNode();
    }

    @Override
    protected boolean isSelected(IntermediateRasterPresentation irp) {
      return irp.isShown();
    }

    private void onRasterClicked(IntermediateRasterPresentation irp) {
      sLogger.userInteraction("Click on raster " + irp.getName());
      mOnRasterListingClickedInteractorFactory.create(irp).execute();
    }
  }
}

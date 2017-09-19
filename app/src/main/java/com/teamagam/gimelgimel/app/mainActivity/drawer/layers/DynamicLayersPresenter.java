package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.mainActivity.drawer.LayersNodeDisplayer;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.OnDynamicLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import io.reactivex.Observable;

@AutoFactory
public class DynamicLayersPresenter extends DrawerCategoryPresenter<DynamicLayerPresentation> {

  private final Context mContext;
  private final DisplayDynamicLayersInteractorFactory mDisplayDynamicLayersInteractorFactory;
  private final OnDynamicLayerListingClickInteractorFactory
      mOnDynamicLayerListingClickInteractorFactory;
  private final SubjectRepository<DynamicLayerPresentation> mSubjectRepository;
  private final Navigator mNavigator;
  private final NewDynamicLayerDialogDisplayer mNewDynamicLayerDialogDisplayer;
  private DisplayDynamicLayersInteractor mDisplayDynamicLayersInteractor;
  private DynamicLayerNodeDisplayer mDynamicLayerNodeDisplayer;

  DynamicLayersPresenter(@Provided Context context,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          OnDynamicLayerListingClickInteractorFactory onDynamicLayerListingClickInteractorFactory,
      @Provided Navigator navigator,
      NewDynamicLayerDialogDisplayer newDynamicLayerDialogDisplayer,
      LayersNodeDisplayer layersNodeDisplayer) {
    super(layersNodeDisplayer);
    mContext = context;
    mDisplayDynamicLayersInteractorFactory = displayDynamicLayersInteractorFactory;
    mOnDynamicLayerListingClickInteractorFactory = onDynamicLayerListingClickInteractorFactory;
    mNavigator = navigator;
    mNewDynamicLayerDialogDisplayer = newDynamicLayerDialogDisplayer;
    mSubjectRepository = SubjectRepository.createSimpleSubject();
    mDynamicLayerNodeDisplayer = new DynamicLayerNodeDisplayer(layersNodeDisplayer);
  }

  @Override
  public void start() {
    mDisplayDynamicLayersInteractor =
        mDisplayDynamicLayersInteractorFactory.create(mSubjectRepository::add);
    mDisplayDynamicLayersInteractor.execute();
  }

  @Override
  public void stop() {
    mDisplayDynamicLayersInteractor.unsubscribe();
  }

  @Override
  protected String getCategoryTitle() {
    return mContext.getString(R.string.drawer_layers_category_name_dynamic_layers);
  }

  @Override
  protected Drawable getCategoryIcon() {
    return createAddDynamicLayerIcon();
  }

  @Override
  protected View.OnClickListener getCategoryIconClickListener() {
    return view -> onAddDynamicLayerClicked();
  }

  @Override
  protected Observable<DynamicLayerPresentation> getItemObservable() {
    return mSubjectRepository.getObservable();
  }

  private Drawable createAddDynamicLayerIcon() {
    Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_plus);
    DrawableCompat.setTint(drawable, ContextCompat.getColor(mContext, R.color.md_light_green_A700));
    return drawable;
  }

  private void onAddDynamicLayerClicked() {
    mNewDynamicLayerDialogDisplayer.display();
  }

  @Override
  protected NodeSelectionDisplayer<DynamicLayerPresentation> getNodeDisplayer() {
    return mDynamicLayerNodeDisplayer;
  }

  public interface NewDynamicLayerDialogDisplayer {
    void display();
  }

  private class DynamicLayerNodeDisplayer extends NodeSelectionDisplayer<DynamicLayerPresentation> {

    private Drawable mEditDrawable;

    public DynamicLayerNodeDisplayer(LayersNodeDisplayer layerNodeDisplayer) {
      super(layerNodeDisplayer);
      mEditDrawable = createEditDrawable();
    }

    @Override
    protected LayersNodeDisplayer.Node createNode(DynamicLayerPresentation item,
        LayersNodeDisplayer.Node parentNode) {
      return new LayersNodeDisplayer.NodeBuilder(item.getName()).setParentId(parentNode.getId())
          .setOnListingClickListener(v -> onDynamicLayerListingClicked(item))
          .setIsSelected(item.isShown())
          .setIcon(mEditDrawable)
          .setOnIconClickListener(view -> onEditLayerClicked(item))
          .setOnListingClickListener(view -> onDynamicLayerClicked(item))
          .createNode();
    }

    private void onDynamicLayerListingClicked(DynamicLayer dynamicLayer) {
      mOnDynamicLayerListingClickInteractorFactory.create(dynamicLayer.getId()).execute();
    }

    private Drawable createEditDrawable() {
      Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.ic_edit);
      DrawableCompat.setTint(drawable,
          ContextCompat.getColor(mContext, R.color.drawer_layers_edit));
      return drawable;
    }

    private void onEditLayerClicked(DynamicLayer dl) {
      mNavigator.openDynamicLayerEditAction(dl);
    }

    private void onDynamicLayerClicked(DynamicLayer dl) {
      mOnDynamicLayerListingClickInteractorFactory.create(dl.getId()).execute();
    }

    @Override
    protected boolean isSelected(DynamicLayerPresentation item) {
      return item.isShown();
    }
  }
}

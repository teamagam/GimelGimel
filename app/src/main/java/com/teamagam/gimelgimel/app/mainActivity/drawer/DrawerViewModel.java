package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.mainActivity.drawer.adapters.UserLocationsRecyclerAdapter;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.OnDynamicLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.OnVectorLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractorFactory;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import com.teamagam.gimelgimel.domain.rasters.OnRasterListingClickedInteractorFactory;
import com.teamagam.gimelgimel.domain.user.OnUserListingClickedInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;
  private final DisplayUserLocationsInteractorFactory mDisplayUserLocationsInteractorFactory;
  private final DisplayDynamicLayersInteractorFactory mDisplayDynamicLayersInteractorFactory;

  private final OnVectorLayerListingClickInteractorFactory
      mOnVectorLayerListingClickInteractorFactory;
  private final OnRasterListingClickedInteractorFactory mOnRasterListingClickedInteractorFactory;
  private final OnUserListingClickedInteractorFactory mOnUserListingClickedInteractorFactory;
  private final OnDynamicLayerListingClickInteractorFactory
      mOnDynamicLayerListingClickInteractorFactory;

  private final UserPreferencesRepository mUserPreferencesRepository;
  private final Context mContext;
  private final LayersNodeDisplayer mLayersNodeDisplayer;

  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;
  private DisplayUserLocationsInteractor mDisplayUserLocationsInteractor;
  private DisplayDynamicLayersInteractor mDisplayDynamicLayersInteractor;

  private UserLocationsRecyclerAdapter mUsersAdapter;
  private String mDynamicLayersCategoryNodeId;
  private String mStaticLayersCategoryNodeId;
  private String mBubbleLayersCategoryNodeId;
  private String mRastersCategoryNodeId;

  private Map<String, String> mDisplayedEntitiesToNodeIdsMap;
  private boolean mIsUsersDisplayed;

  public DrawerViewModel(
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayUserLocationsInteractorFactory displayUserLocationsInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          OnVectorLayerListingClickInteractorFactory onVectorLayerListingClickInteractorFactory,
      @Provided OnRasterListingClickedInteractorFactory onRasterListingClickedInteractorFactory,
      @Provided OnUserListingClickedInteractorFactory onUserListingClickedInteractorFactory,
      @Provided
          OnDynamicLayerListingClickInteractorFactory onDynamicLayerListingClickInteractorFactory,
      @Provided UserPreferencesRepository userPreferencesRepository,
      Context context,
      LayersNodeDisplayer layersNodeDisplayer) {
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mDisplayDynamicLayersInteractorFactory = displayDynamicLayersInteractorFactory;
    mOnVectorLayerListingClickInteractorFactory = onVectorLayerListingClickInteractorFactory;
    mOnRasterListingClickedInteractorFactory = onRasterListingClickedInteractorFactory;
    mDisplayUserLocationsInteractorFactory = displayUserLocationsInteractorFactory;
    mOnUserListingClickedInteractorFactory = onUserListingClickedInteractorFactory;
    mOnDynamicLayerListingClickInteractorFactory = onDynamicLayerListingClickInteractorFactory;
    mUserPreferencesRepository = userPreferencesRepository;
    mContext = context;
    mLayersNodeDisplayer = layersNodeDisplayer;
    mDisplayedEntitiesToNodeIdsMap = new HashMap<>();
    mIsUsersDisplayed = true;
  }

  @Override
  public void start() {
    super.start();
    initializeDisplayInteractors();
    initializeAdapters();
    initializeLayerCategories();
  }

  @Bindable
  public boolean isUsersDisplayed() {
    return mIsUsersDisplayed;
  }

  public String getUsername() {
    return mUserPreferencesRepository.getString(mContext.getString(R.string.user_name_text_key));
  }

  public List<NavigationTabBar.Model> getTabModels() {
    return Arrays.asList(
        createModel(R.string.drawer_nav_bar_users_title, R.drawable.ic_user, R.color.themeRed),
        createModel(R.string.drawer_nav_bar_layers_title, R.drawable.ic_layers, R.color.themeBlue));
  }

  public void onNavigationTabBarSelected(int index) {
    switch (index) {
      case 0:
        onUsersTabSelected();
        break;
      case 1:
        onLayersTabSelected();
        break;
      default:
        throw new RuntimeException("Unmapped tab selected - index: " + index);
    }
  }

  public void resume() {
    mDisplayVectorLayersInteractor.execute();
    mDisplayIntermediateRastersInteractor.execute();
    mDisplayUserLocationsInteractor.execute();
    mDisplayDynamicLayersInteractor.execute();
  }

  public void pause() {
    mDisplayVectorLayersInteractor.unsubscribe();
    mDisplayIntermediateRastersInteractor.unsubscribe();
    mDisplayUserLocationsInteractor.unsubscribe();
    mDisplayDynamicLayersInteractor.unsubscribe();
  }

  private void initializeDisplayInteractors() {
    mDisplayVectorLayersInteractor =
        mDisplayVectorLayersInteractorFactory.create(new DrawerVectorLayersDisplayer());
    mDisplayIntermediateRastersInteractor =
        mDisplayIntermediateRastersInteractorFactory.create(new IntermediateRasterDisplayer());
    mDisplayUserLocationsInteractor =
        mDisplayUserLocationsInteractorFactory.create(new UserLocationsDisplayer());
    mDisplayDynamicLayersInteractor =
        mDisplayDynamicLayersInteractorFactory.create(new DrawerTreeViewDynamicLayerDisplayer());
  }

  private void initializeAdapters() {
    mUsersAdapter = new UserLocationsRecyclerAdapter(this::onUserClicked);
  }

  private void initializeLayerCategories() {
    LayersNodeDisplayer.Node dynamicLayers = createCategoryNode("Dynamic Layer");
    mDynamicLayersCategoryNodeId = dynamicLayers.getId();
    mLayersNodeDisplayer.addNode(dynamicLayers);

    LayersNodeDisplayer.Node staticLayers = createCategoryNode("Static Layers");
    mStaticLayersCategoryNodeId = staticLayers.getId();
    mLayersNodeDisplayer.addNode(staticLayers);

    LayersNodeDisplayer.Node bubbleLayers = createCategoryNode("Bubble Layers");
    mBubbleLayersCategoryNodeId = bubbleLayers.getId();
    mLayersNodeDisplayer.addNode(bubbleLayers);

    LayersNodeDisplayer.Node rasters = createCategoryNode("Rasters");
    mRastersCategoryNodeId = rasters.getId();
    mLayersNodeDisplayer.addNode(rasters);
  }

  private LayersNodeDisplayer.Node createCategoryNode(String title) {
    return new LayersNodeDisplayer.NodeBuilder().setTitle(title).createNode();
  }

  private void onUsersTabSelected() {
    sLogger.userInteraction("Users tab selected");
    setIsUsersDisplayed(true);
  }

  private void setIsUsersDisplayed(boolean isDisplayed) {
    if (mIsUsersDisplayed != isDisplayed) {
      mIsUsersDisplayed = isDisplayed;
      notifyPropertyChanged(BR.usersDisplayed);
    }
  }

  private void onLayersTabSelected() {
    sLogger.userInteraction("Layers tab selected");
    setIsUsersDisplayed(false);
  }

  private NavigationTabBar.Model createModel(int titleId, int drawableId, int colorId) {
    return new NavigationTabBar.Model.Builder(getDrawable(drawableId), getColor(colorId)).title(
        getString(titleId)).build();
  }

  private Drawable getDrawable(int drawableId) {
    return ContextCompat.getDrawable(mContext, drawableId);
  }

  private int getColor(int colorId) {
    return ContextCompat.getColor(mContext, colorId);
  }

  private String getString(int titleId) {
    return mContext.getString(titleId);
  }

  private void onUserClicked(UserLocation userLocation) {
    sLogger.userInteraction("Click on user " + userLocation.getUser());
    mOnUserListingClickedInteractorFactory.create(userLocation).execute();
  }

  public RecyclerView.Adapter getUsersAdapter() {
    return mUsersAdapter;
  }

  private class UserLocationsDisplayer implements DisplayUserLocationsInteractor.Displayer {
    @Override
    public void displayActive(UserLocation userLocation) {
      updateUserLocation(userLocation);
    }

    @Override
    public void displayStale(UserLocation userLocation) {
      updateUserLocation(userLocation);
    }

    @Override
    public void displayIrrelevant(UserLocation userLocation) {
      String userLocationId =
          new UserLocationsRecyclerAdapter.UserLocationAdapter(userLocation).getId();
      if (mUsersAdapter.contains(userLocationId)) {
        mUsersAdapter.remove(userLocationId);
      }
    }

    private void updateUserLocation(UserLocation userLocation) {
      mUsersAdapter.show(new UserLocationsRecyclerAdapter.UserLocationAdapter(userLocation));
    }
  }

  private class IntermediateRasterDisplayer
      implements DisplayIntermediateRastersInteractor.Displayer {

    private NodeSelectionDisplayer<IntermediateRasterPresentation> mInnerDisplayer =
        new NodeSelectionDisplayer<IntermediateRasterPresentation>() {
          @Override
          protected LayersNodeDisplayer.Node createNode(IntermediateRasterPresentation item) {
            return new LayersNodeDisplayer.NodeBuilder().setParentId(mRastersCategoryNodeId)
                .setIsSelected(item.isShown())
                .setTitle(item.getName())
                .setOnListingClickListener(view -> onRasterClicked(item))
                .createNode();
          }

          @Override
          protected boolean getSelectionState(IntermediateRasterPresentation item) {
            return item.isShown();
          }

          @Override
          protected String getItemId(IntermediateRasterPresentation item) {
            return item.getName();
          }

          private void onRasterClicked(IntermediateRasterPresentation irp) {
            sLogger.userInteraction("Click on raster " + irp.getName());
            mOnRasterListingClickedInteractorFactory.create(irp).execute();
          }
        };

    @Override
    public void display(IntermediateRasterPresentation raster) {
      mInnerDisplayer.display(raster);
    }
  }

  private class DrawerVectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {

    private NodeSelectionDisplayer<VectorLayerPresentation> mInnerDisplayer =
        new NodeSelectionDisplayer<VectorLayerPresentation>() {
          @Override
          protected LayersNodeDisplayer.Node createNode(VectorLayerPresentation item) {
            return new LayersNodeDisplayer.NodeBuilder().setParentId(getCategoryId(item))
                .setIsSelected(item.isShown())
                .setTitle(item.getName())
                .setOnListingClickListener(view -> onVectorLayerClicked(item))
                .createNode();
          }

          @Override
          protected boolean getSelectionState(VectorLayerPresentation item) {
            return item.isShown();
          }

          @Override
          protected String getItemId(VectorLayerPresentation item) {
            return item.getName();
          }

          private String getCategoryId(VectorLayerPresentation item) {
            return isBubbleLayer(item) ? mBubbleLayersCategoryNodeId : mStaticLayersCategoryNodeId;
          }

          private boolean isBubbleLayer(VectorLayerPresentation vlp) {
            return vlp.getCategory() == VectorLayer.Category.FIRST;
          }

          private void onVectorLayerClicked(VectorLayerPresentation vlp) {
            sLogger.userInteraction("Click on vl " + vlp.getName());
            mOnVectorLayerListingClickInteractorFactory.create(vlp).execute();
          }
        };

    @Override
    public void display(VectorLayerPresentation vlp) {
      mInnerDisplayer.display(vlp);
    }
  }

  private class DrawerTreeViewDynamicLayerDisplayer
      implements DisplayDynamicLayersInteractor.Displayer {

    private NodeSelectionDisplayer<DynamicLayerPresentation> mInnerDisplayer =
        new NodeSelectionDisplayer<DynamicLayerPresentation>() {
          @Override
          protected LayersNodeDisplayer.Node createNode(DynamicLayerPresentation dlp) {
            return new LayersNodeDisplayer.NodeBuilder().setTitle(dlp.getName())
                .setParentId(mDynamicLayersCategoryNodeId)
                .setIsSelected(dlp.isShown())
                .setOnListingClickListener(view -> onDynamicLayerClicked(dlp))
                .createNode();
          }

          @Override
          protected boolean getSelectionState(DynamicLayerPresentation dlp) {
            return dlp.isShown();
          }

          @Override
          protected String getItemId(DynamicLayerPresentation dlp) {
            return dlp.getId();
          }

          private void onDynamicLayerClicked(DynamicLayer dl) {
            mOnDynamicLayerListingClickInteractorFactory.create(dl).execute();
          }
        };

    @Override
    public void display(DynamicLayerPresentation dl) {
      mInnerDisplayer.display(dl);
    }
  }

  private abstract class NodeSelectionDisplayer<T> {

    public synchronized void display(T item) {
      if (alreadyExists(item)) {
        mLayersNodeDisplayer.setNodeSelectionState(
            mDisplayedEntitiesToNodeIdsMap.get(getItemId(item)), getSelectionState(item));
      } else {
        LayersNodeDisplayer.Node node = createNode(item);
        mLayersNodeDisplayer.addNode(node);
        mDisplayedEntitiesToNodeIdsMap.put(getItemId(item), node.getId());
      }
    }

    protected abstract LayersNodeDisplayer.Node createNode(T item);

    protected abstract boolean getSelectionState(T item);

    protected abstract String getItemId(T item);

    protected boolean alreadyExists(T item) {
      return mDisplayedEntitiesToNodeIdsMap.containsKey(getItemId(item));
    }
  }
}

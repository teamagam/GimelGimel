package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.content.Context;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.google.common.collect.Lists;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.mainActivity.drawer.layers.DrawerCategoryPresenter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.layers.DynamicLayersCategoryPresenter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.layers.LayersNodeDisplayer;
import com.teamagam.gimelgimel.app.mainActivity.drawer.users.UserLocationsRecyclerAdapter;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteCreationDynamicLayerRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.OnVectorLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractorFactory;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.user.OnUserListingClickedInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import io.reactivex.functions.Predicate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

  private static final int NEW_DYNAMIC_LAYER_MINIMUM_LENGTH = 1;
  private static final int NEW_DYNAMIC_LAYER_MAXIMUM_LENGTH = 10;

  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final DisplayUserLocationsInteractorFactory mDisplayUserLocationsInteractorFactory;

  private final OnVectorLayerListingClickInteractorFactory
      mOnVectorLayerListingClickInteractorFactory;
  private final OnUserListingClickedInteractorFactory mOnUserListingClickedInteractorFactory;

  private final SendRemoteCreationDynamicLayerRequestInteractorFactory
      mSendRemoteCreationDynamicLayerRequestInteractorFactory;

  private final UserPreferencesRepository mUserPreferencesRepository;
  private final Context mContext;
  private final LayersNodeDisplayer mLayersNodeDisplayer;

  private final Iterable<DrawerCategoryPresenter> mLayerPresenters;
  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private DisplayUserLocationsInteractor mDisplayUserLocationsInteractor;
  private UserLocationsRecyclerAdapter mUsersAdapter;
  private String mStaticLayersCategoryNodeId;
  private String mBubbleLayersCategoryNodeId;
  private Map<String, String> mDisplayedEntitiesToNodeIdsMap;
  private boolean mIsUsersDisplayed;

  public DrawerViewModel(
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayUserLocationsInteractorFactory displayUserLocationsInteractorFactory,
      @Provided
          OnVectorLayerListingClickInteractorFactory onVectorLayerListingClickInteractorFactory,
      @Provided OnUserListingClickedInteractorFactory onUserListingClickedInteractorFactory,
      @Provided
          SendRemoteCreationDynamicLayerRequestInteractorFactory sendRemoteCreationDynamicLayerRequestInteractorFactory,
      @Provided UserPreferencesRepository userPreferencesRepository,
      @Provided
          com.teamagam.gimelgimel.app.mainActivity.drawer.layers.DynamicLayersCategoryPresenterFactory dynamicLayersPresenterFactory,
      @Provided
          com.teamagam.gimelgimel.app.mainActivity.drawer.layers.RasterCategoryPresenterFactory rasterCategoryPresenterFactory,
      Context context,
      LayersNodeDisplayer layersNodeDisplayer,
      DynamicLayersCategoryPresenter.NewDynamicLayerDialogDisplayer newDynamicLayerDialogDisplayer) {
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mOnVectorLayerListingClickInteractorFactory = onVectorLayerListingClickInteractorFactory;
    mDisplayUserLocationsInteractorFactory = displayUserLocationsInteractorFactory;
    mOnUserListingClickedInteractorFactory = onUserListingClickedInteractorFactory;
    mSendRemoteCreationDynamicLayerRequestInteractorFactory =
        sendRemoteCreationDynamicLayerRequestInteractorFactory;
    mUserPreferencesRepository = userPreferencesRepository;
    mContext = context;
    mLayersNodeDisplayer = layersNodeDisplayer;
    mDisplayedEntitiesToNodeIdsMap = new HashMap<>();
    mIsUsersDisplayed = true;
    mLayerPresenters = Lists.newArrayList(
        dynamicLayersPresenterFactory.create(newDynamicLayerDialogDisplayer, layersNodeDisplayer),
        rasterCategoryPresenterFactory.create(layersNodeDisplayer));
  }

  @Override
  public void start() {
    super.start();
    initializeDisplayInteractors();
    initializeUsersAdapter();
    initializeLayerCategories();
    for (DrawerCategoryPresenter presenter : mLayerPresenters) {
      presenter.start();
      presenter.presentCategory();
      presenter.presentCategoryItems();
    }
  }

  @Override
  public void stop() {
    super.stop();
    for (DrawerCategoryPresenter presenter : mLayerPresenters) {
      presenter.stop();
    }
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
    mDisplayUserLocationsInteractor.execute();
  }

  public void pause() {
    mDisplayVectorLayersInteractor.unsubscribe();
    mDisplayUserLocationsInteractor.unsubscribe();
  }

  public void onNewDynamicLayerDialogOkClicked(String name) {
    sLogger.userInteraction("New dynamic layer requested with name = " + name);
    mSendRemoteCreationDynamicLayerRequestInteractorFactory.create(name).execute();
  }

  public RecyclerView.Adapter getUsersAdapter() {
    return mUsersAdapter;
  }

  public Predicate<String> getDynamicLayerTextValidator() {
    return name -> isLengthBetween(name, NEW_DYNAMIC_LAYER_MINIMUM_LENGTH,
        NEW_DYNAMIC_LAYER_MAXIMUM_LENGTH);
  }

  private boolean isLengthBetween(String name, int min, int max) {
    return name.length() >= min && name.length() <= max;
  }

  private void initializeDisplayInteractors() {
    mDisplayVectorLayersInteractor =
        mDisplayVectorLayersInteractorFactory.create(new DrawerTreeViewVectorLayerDisplayer());
    mDisplayUserLocationsInteractor =
        mDisplayUserLocationsInteractorFactory.create(new UserLocationsDisplayer());
  }

  private void initializeUsersAdapter() {
    mUsersAdapter = new UserLocationsRecyclerAdapter(this::onUserClicked);
  }

  private void initializeLayerCategories() {
    LayersNodeDisplayer.Node staticLayers =
        createCategoryNode(R.string.drawer_layers_category_name_static_layers);
    mStaticLayersCategoryNodeId = staticLayers.getId();
    mLayersNodeDisplayer.addNode(staticLayers);

    LayersNodeDisplayer.Node bubbleLayers =
        createCategoryNode(R.string.drawer_layers_category_name_bubble_layers);
    mBubbleLayersCategoryNodeId = bubbleLayers.getId();
    mLayersNodeDisplayer.addNode(bubbleLayers);
  }

  private LayersNodeDisplayer.Node createCategoryNode(int stringResId,
      Drawable icon,
      View.OnClickListener listener) {
    return new LayersNodeDisplayer.NodeBuilder(mContext.getString(stringResId)).setIcon(icon)
        .setOnIconClickListener(listener)
        .createNode();
  }

  private LayersNodeDisplayer.Node createCategoryNode(int stringResId) {
    return createCategoryNode(stringResId, null, null);
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

  private class DrawerTreeViewVectorLayerDisplayer
      implements DisplayVectorLayersInteractor.Displayer {

    private NodeSelectionDisplayer<VectorLayerPresentation> mInnerDisplayer =
        new NodeSelectionDisplayer<VectorLayerPresentation>() {
          @Override
          protected LayersNodeDisplayer.Node createNode(VectorLayerPresentation vlp) {
            return new LayersNodeDisplayer.NodeBuilder(vlp.getName()).setParentId(
                getCategoryId(vlp))
                .setIsSelected(vlp.isShown())
                .setOnListingClickListener(view -> onVectorLayerClicked(vlp))
                .createNode();
          }

          @Override
          protected boolean getSelectionState(VectorLayerPresentation vlp) {
            return vlp.isShown();
          }

          @Override
          protected String getItemId(VectorLayerPresentation vlp) {
            return vlp.getName();
          }

          private String getCategoryId(VectorLayerPresentation vlp) {
            return isBubbleLayer(vlp) ? mBubbleLayersCategoryNodeId : mStaticLayersCategoryNodeId;
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

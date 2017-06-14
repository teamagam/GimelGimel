package com.teamagam.gimelgimel.app.mainActivity.drawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.mainActivity.drawer.adapters.LayersRecyclerAdapter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.adapters.RastersRecyclerAdapter;
import com.teamagam.gimelgimel.app.mainActivity.drawer.adapters.UserLocationsRecyclerAdapter;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.OnVectorLayerListingClickInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractorFactory;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterPresentation;
import com.teamagam.gimelgimel.domain.rasters.OnRasterListingClickedInteractorFactory;
import com.teamagam.gimelgimel.domain.user.OnUserListingClickedInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;
  private final DisplayUserLocationsInteractorFactory mDisplayUserLocationsInteractorFactory;

  private final OnVectorLayerListingClickInteractorFactory
      mOnVectorLayerListingClickInteractorFactory;
  private final OnRasterListingClickedInteractorFactory mOnRasterListingClickedInteractorFactory;
  private final OnUserListingClickedInteractorFactory mOnUserListingClickedInteractorFactory;

  private final UserPreferencesRepository mUserPreferencesRepository;
  private final Context mContext;
  private final RecyclerViewAdapterSetter mAdapterSetter;

  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;
  private DisplayUserLocationsInteractor mDisplayUserLocationsInteractor;

  private UserLocationsRecyclerAdapter mUsersAdapter;
  private LayersRecyclerAdapter mBubbleLayersAdapter;
  private LayersRecyclerAdapter mLayersAdapter;
  private RastersRecyclerAdapter mRastersAdapter;

  public DrawerViewModel(
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayUserLocationsInteractorFactory displayUserLocationsInteractorFactory,
      @Provided
          OnVectorLayerListingClickInteractorFactory onVectorLayerListingClickInteractorFactory,
      @Provided OnRasterListingClickedInteractorFactory onRasterListingClickedInteractorFactory,
      @Provided OnUserListingClickedInteractorFactory onUserListingClickedInteractorFactory,
      @Provided UserPreferencesRepository userPreferencesRepository,
      Context context,
      RecyclerViewAdapterSetter adapterSetter) {
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mOnVectorLayerListingClickInteractorFactory = onVectorLayerListingClickInteractorFactory;
    mOnRasterListingClickedInteractorFactory = onRasterListingClickedInteractorFactory;
    mDisplayUserLocationsInteractorFactory = displayUserLocationsInteractorFactory;
    mOnUserListingClickedInteractorFactory = onUserListingClickedInteractorFactory;
    mUserPreferencesRepository = userPreferencesRepository;
    mContext = context;
    mAdapterSetter = adapterSetter;
  }

  @Override
  public void start() {
    super.start();
    initializeDisplayInteractors();
    initializeAdapters();
  }

  public String getUsername() {
    return mUserPreferencesRepository.getString(mContext.getString(R.string.user_name_text_key));
  }

  public List<NavigationTabBar.Model> getTabModels() {
    List<NavigationTabBar.Model> models = new ArrayList<>(4);

    models.add(
        createModel(R.string.drawer_nav_bar_users_title, R.drawable.ic_user, R.color.themeRed));
    models.add(createModel(R.string.drawer_nav_bar_bubble_layers_title, R.drawable.ic_bubbles,
        R.color.themeYellow));
    models.add(
        createModel(R.string.drawer_nav_bar_layers_title, R.drawable.ic_layers, R.color.themeBlue));
    models.add(
        createModel(R.string.drawer_nav_bar_rasters_title, R.drawable.ic_map, R.color.themePink));

    return models;
  }

  public void onNavigationTabBarSelected(int index) {
    switch (index) {
      case 0:
        onUsersTabSelected();
        break;
      case 1:
        onBubbleLayersTabSelected();
        break;
      case 2:
        onLayersTabSelected();
        break;
      case 3:
        onRastersTabSelected();
        break;
      default:
        throw new RuntimeException("Unknown tab selected - index: " + index);
    }
  }

  public void resume() {
    mDisplayVectorLayersInteractor.execute();
    mDisplayIntermediateRastersInteractor.execute();
    mDisplayUserLocationsInteractor.execute();
  }

  public void pause() {
    mDisplayVectorLayersInteractor.unsubscribe();
    mDisplayIntermediateRastersInteractor.unsubscribe();
    mDisplayUserLocationsInteractor.unsubscribe();
  }

  private void initializeDisplayInteractors() {
    mDisplayVectorLayersInteractor =
        mDisplayVectorLayersInteractorFactory.create(new DrawerVectorLayersDisplayer());
    mDisplayIntermediateRastersInteractor =
        mDisplayIntermediateRastersInteractorFactory.create(new IntermediateRasterDisplayer());
    mDisplayUserLocationsInteractor =
        mDisplayUserLocationsInteractorFactory.create(new UserLocationsDisplayer());
  }

  private void initializeAdapters() {
    mLayersAdapter = new LayersRecyclerAdapter(this::onVectorLayerClicked);
    mBubbleLayersAdapter = new LayersRecyclerAdapter(this::onVectorLayerClicked);
    mRastersAdapter = new RastersRecyclerAdapter(this::onRasterClicked);
    mUsersAdapter = new UserLocationsRecyclerAdapter(this::onUserClicked);
  }

  private void onUsersTabSelected() {
    sLogger.userInteraction("Users selected");

    mAdapterSetter.setAdapter(mUsersAdapter);
  }

  private void onBubbleLayersTabSelected() {
    sLogger.userInteraction("Bubble layers selected");
    mAdapterSetter.setAdapter(mBubbleLayersAdapter);
  }

  private void onLayersTabSelected() {
    sLogger.userInteraction("Layers selected");
    mAdapterSetter.setAdapter(mLayersAdapter);
  }

  private void onRastersTabSelected() {
    sLogger.userInteraction("Rasters selected");
    mAdapterSetter.setAdapter(mRastersAdapter);
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

  private void onVectorLayerClicked(VectorLayerPresentation vlp) {
    sLogger.userInteraction("Click on vl " + vlp.getName());
    mOnVectorLayerListingClickInteractorFactory.create(vlp).execute();
  }

  private void onRasterClicked(IntermediateRasterPresentation irp) {
    sLogger.userInteraction("Click on raster " + irp.getName());
    mOnRasterListingClickedInteractorFactory.create(irp).execute();
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

  private class IntermediateRasterDisplayer
      implements DisplayIntermediateRastersInteractor.Displayer {
    @Override
    public void display(IntermediateRasterPresentation raster) {
      mRastersAdapter.show(new RastersRecyclerAdapter.IdentifiedRasterAdapter(raster));
    }
  }

  private class DrawerVectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {
    @Override
    public void display(VectorLayerPresentation vlp) {
      LayersRecyclerAdapter.IdentifiedVectorLayerAdapter ivlp =
          new LayersRecyclerAdapter.IdentifiedVectorLayerAdapter(vlp);
      if (isBubbleLayer(vlp)) {
        mBubbleLayersAdapter.show(ivlp);
      } else {
        mLayersAdapter.show(ivlp);
      }
    }

    private boolean isBubbleLayer(VectorLayerPresentation vlp) {
      return vlp.getCategory() == VectorLayer.Category.FIRST;
    }
  }

  public interface RecyclerViewAdapterSetter {
    void setAdapter(RecyclerView.Adapter adapter);
  }
}

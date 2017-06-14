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
import com.teamagam.gimelgimel.domain.layers.SetVectorLayerVisibilityInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.DisplayUserLocationsInteractorFactory;
import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.SetIntermediateRasterInteractorFactory;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import devlight.io.library.ntb.NavigationTabBar;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class DrawerViewModel extends BaseViewModel<MainActivityDrawer> {

  private final DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;
  private final SetVectorLayerVisibilityInteractorFactory
      mSetVectorLayerVisibilityInteractorFactory;
  private final DisplayIntermediateRastersInteractorFactory
      mDisplayIntermediateRastersInteractorFactory;
  private final SetIntermediateRasterInteractorFactory mSetIntermediateRasterInteractorFactory;
  private final DisplayUserLocationsInteractorFactory mDisplayUserLocationsInteractorFactory;
  private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;

  private final UserPreferencesRepository mUserPreferencesRepository;
  private final Context mContext;

  private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
  private DisplayIntermediateRastersInteractor mDisplayIntermediateRastersInteractor;
  private DisplayUserLocationsInteractor mDisplayUserLocationsInteractor;

  private RecyclerViewAdapterSetter mAdapterSetter;

  private UserLocationsRecyclerAdapter mUsersAdapter;
  private LayersRecyclerAdapter mBubbleLayersAdapter;
  private LayersRecyclerAdapter mLayersAdapter;
  private RastersRecyclerAdapter mRastersAdapter;

  public DrawerViewModel(
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided SetVectorLayerVisibilityInteractorFactory setVectorLayerVisibilityInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided SetIntermediateRasterInteractorFactory setIntermediateRasterInteractorFactory,
      @Provided DisplayUserLocationsInteractorFactory displayUserLocationsInteractorFactory,
      @Provided GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
      @Provided UserPreferencesRepository userPreferencesRepository,
      Context context,
      RecyclerViewAdapterSetter adapterSetter) {
    mDisplayVectorLayersInteractorFactory = displayVectorLayersInteractorFactory;
    mSetVectorLayerVisibilityInteractorFactory = setVectorLayerVisibilityInteractorFactory;
    mDisplayIntermediateRastersInteractorFactory = displayIntermediateRastersInteractorFactory;
    mSetIntermediateRasterInteractorFactory = setIntermediateRasterInteractorFactory;
    mDisplayUserLocationsInteractorFactory = displayUserLocationsInteractorFactory;
    mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
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

  private void onVectorLayerClicked(VectorLayerPresentation vectorLayer) {
    VectorLayerVisibilityChange change =
        new VectorLayerVisibilityChange(vectorLayer.getId(), !vectorLayer.isShown());
    mSetVectorLayerVisibilityInteractorFactory.create(change).execute();
  }

  private void onRasterClicked(DisplayIntermediateRastersInteractor.IntermediateRasterPresentation raster) {
    if (isCurrentlyHidden(raster)) {
      mSetIntermediateRasterInteractorFactory.create(raster.getName()).execute();
    } else {
      mSetIntermediateRasterInteractorFactory.create(null).execute();
    }
  }

  private boolean isCurrentlyHidden(DisplayIntermediateRastersInteractor.IntermediateRasterPresentation raster) {
    return !raster.isShown();
  }

  private void onUserClicked(UserLocation userLocation) {
    sLogger.userInteraction("Click on user " + userLocation.getUser());
    mGoToLocationMapInteractorFactory.create(userLocation.getLocationSample().getLocation())
        .execute();
  }

  public interface RecyclerViewAdapterSetter {
    void setAdapter(RecyclerView.Adapter adapter);
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
    public void display(DisplayIntermediateRastersInteractor.IntermediateRasterPresentation raster) {
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
}

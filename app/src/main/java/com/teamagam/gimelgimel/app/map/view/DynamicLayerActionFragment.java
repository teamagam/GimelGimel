package com.teamagam.gimelgimel.app.map.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import com.roughike.bottombar.BottomBar;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.DynamicLayerEditViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.DynamicLayerEditViewModelFactory;
import com.teamagam.gimelgimel.databinding.FragmentDynamicLayerActionBinding;
import javax.inject.Inject;

public class DynamicLayerActionFragment
    extends DrawGeometryActionFragment<DynamicLayerEditViewModel> {

  @Inject
  DynamicLayerEditViewModelFactory mViewModelFactory;

  @BindView(R.id.edit_dynamic_layer_map_view)
  GGMapView mGGMapView;
  @BindView(R.id.bottombar_geometry_type)
  BottomBar mBottomBar;

  private DynamicLayerEditViewModel mViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);

    mViewModel = mViewModelFactory.create(mGGMapView, super::pickColor, super::pickBorderStyle);
    mViewModel.init();

    setBottomBarListeners();

    FragmentDynamicLayerActionBinding bind = FragmentDynamicLayerActionBinding.bind(view);
    bind.setViewModel(mViewModel);

    return bind.getRoot();
  }

  private void setBottomBarListeners() {
    mBottomBar.setOnTabSelectListener((tabResource) -> mViewModel.onNewTabSelection(tabResource),
        true);
    mBottomBar.setTabSelectionInterceptor((oldTabId, newTabId) -> {
      if (mViewModel.isOnEditMode()) {
        Toast.makeText(getContext(), R.string.dynamic_layer_switch_geometry_warning,
            Toast.LENGTH_LONG).show();
        return true;
      }

      return false;
    });
  }

  @Override
  public void onStop() {
    super.onStop();

    mBottomBar.removeOnTabSelectListener();
    mBottomBar.removeOverrideTabSelectionListener();
  }

  @Override
  protected DynamicLayerEditViewModel getSpecificViewModel() {
    return mViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.menu_action_dynamic_layer_edit_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_dynamic_layer_action;
  }
}

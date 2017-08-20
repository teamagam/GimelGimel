package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import com.roughike.bottombar.BottomBar;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseStyleDrawActionFragment;
import com.teamagam.gimelgimel.app.map.viewModel.EditDynamicLayerViewModel;
import com.teamagam.gimelgimel.app.map.viewModel.EditDynamicLayerViewModelFactory;
import com.teamagam.gimelgimel.databinding.FragmentDynamicLayerActionBinding;
import javax.inject.Inject;

public class EditDynamicLayerActionFragment
    extends BaseStyleDrawActionFragment<EditDynamicLayerViewModel> {

  private static final String DYNAMIC_LAYER_ID_KEY = "dynamic_layer_id_key";
  @Inject
  EditDynamicLayerViewModelFactory mViewModelFactory;

  @BindView(R.id.edit_dynamic_layer_map_view)
  GGMapView mGGMapView;
  @BindView(R.id.bottombar_geometry_type)
  BottomBar mBottomBar;
  @BindView(R.id.free_draw_pan_mode_switch)
  SwitchCompat mSwitchCompat;

  private EditDynamicLayerViewModel mViewModel;

  public static EditDynamicLayerActionFragment createFragment(String dynamicLayerId) {
    EditDynamicLayerActionFragment fragment = new EditDynamicLayerActionFragment();
    Bundle bundle = new Bundle();
    bundle.putString(DYNAMIC_LAYER_ID_KEY, dynamicLayerId);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);

    mViewModel = mViewModelFactory.create(mGGMapView, this::openDeleteDialog, this::pickColor,
        this::pickBorderStyle, getDynamicLayerId());
    mViewModel.init();

    setBottomBarListeners();

    FragmentDynamicLayerActionBinding bind = FragmentDynamicLayerActionBinding.bind(view);
    bind.setViewModel(mViewModel);

    return bind.getRoot();
  }

  @Override
  public void onStop() {
    super.onStop();

    mBottomBar.removeOnTabSelectListener();
    mBottomBar.removeOverrideTabSelectionListener();
  }

  @Override
  protected EditDynamicLayerViewModel getSpecificViewModel() {
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

  private void onTabSelected(int tabResource) {
    mSwitchCompat.setChecked(false);
    mViewModel.onNewTabSelection(tabResource);
  }

  private String getDynamicLayerId() {
    return getArguments().getString(DYNAMIC_LAYER_ID_KEY);
  }

  private void setBottomBarListeners() {
    mBottomBar.setOnTabSelectListener(this::onTabSelected, true);
    mBottomBar.setTabSelectionInterceptor((oldTabId, newTabId) -> {
      if (mViewModel.isOnEditMode()) {
        Toast.makeText(getContext(), R.string.dynamic_layer_switch_geometry_warning,
            Toast.LENGTH_LONG).show();
        return true;
      }

      return false;
    });
  }

  private void openDeleteDialog(DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(getContext()).setTitle(R.string.dynamic_layer_delete_dialog_title)
        .setMessage(R.string.dynamic_layer_delete_dialog_message)
        .setPositiveButton(android.R.string.ok, okListener)
        .setNegativeButton(android.R.string.cancel, getStubListener())
        .create()
        .show();
  }

  private DialogInterface.OnClickListener getStubListener() {
    return (dialogInterface, i) -> {
    };
  }
}

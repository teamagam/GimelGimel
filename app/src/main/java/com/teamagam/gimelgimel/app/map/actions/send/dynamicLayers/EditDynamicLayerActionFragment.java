package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import com.roughike.bottombar.BottomBar;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.dynamic_layer.DynamicLayerDetailsFragment;
import com.teamagam.gimelgimel.app.icons.IconProvider;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseStyleDrawActionFragment;
import com.teamagam.gimelgimel.databinding.FragmentDynamicLayerActionBinding;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import javax.inject.Inject;

public class EditDynamicLayerActionFragment
    extends BaseStyleDrawActionFragment<EditDynamicLayerViewModel>
    implements DynamicLayerDetailsFragment.OnDynamicEntityClickedListener {

  private static final String DYNAMIC_LAYER_ID_KEY = "dynamic_layer_id_key";
  @Inject
  EditDynamicLayerViewModelFactory mViewModelFactory;
  @Inject
  IconProvider mIconProvider;

  @BindView(R.id.edit_dynamic_layer_map_view)
  GGMapView mGGMapView;
  @BindView(R.id.bottombar_geometry_type)
  BottomBar mBottomBar;
  @BindView(R.id.free_draw_pan_mode_switch)
  SwitchCompat mSwitchCompat;
  @BindView(R.id.edit_dynamic_layer_icon_selection_image)
  ImageView mIconImageView;

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

    initViewModel();
    setBottomBarListeners();
    initDetailsPanel();

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
  public boolean onBackPressed() {
    if (mViewModel.isOnEditMode()) {
      mViewModel.resetAction();
      return true;
    }
    return false;
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

  private void initViewModel() {
    Navigator navigator = new Navigator(getActivity());
    mViewModel =
        mViewModelFactory.create(navigator, mGGMapView, this::openDeleteDialog, this::pickColor,
            this::pickBorderStyle, this::displayIcon, getDynamicLayerId());
    mViewModel.init();
  }

  private void displayIcon(Icon icon) {
    mIconProvider.load(mIconImageView, icon.getId());
  }

  private String getDynamicLayerId() {
    return getArguments().getString(DYNAMIC_LAYER_ID_KEY);
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

  private void onTabSelected(int tabResource) {
    mSwitchCompat.setChecked(false);
    mViewModel.onNewTabSelection(tabResource);
  }

  private void initDetailsPanel() {
    DynamicLayerDetailsFragment dynamicLayerDetailsFragment =
        DynamicLayerDetailsFragment.newInstance(getDynamicLayerId(), null);
    getChildFragmentManager().beginTransaction()
        .add(R.id.dynamic_layer_edit_details_placeholder, dynamicLayerDetailsFragment)
        .commit();
  }

  @Override
  public void onDynamicEntityListingClicked(DynamicEntity dynamicEntity) {
    //nothing for now
  }
}

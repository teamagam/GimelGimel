package com.teamagam.gimelgimel.app.map.actions.send.geometry;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.databinding.FragmentSendGeometryBinding;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.thebluealliance.spectrum.SpectrumDialog;
import java.util.Locale;
import javax.inject.Inject;

public class SendGeometryActionFragment extends BaseDrawActionFragment<SendGeometryViewModel> {

  @Inject
  SendGeometryViewModelFactory mSendGeometryViewModelFactory;

  @BindView(R.id.send_geometry_mapview)
  GGMapView mGGMapView;

  private SendGeometryViewModel mViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);

    initializeViewModel();
    setupMapClickDelegation();

    return bind(view).getRoot();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    mGGMapView.setOnMapGestureListener(null);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_send_geometry;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.send_geometry_toolbar_title);
  }

  @Override
  protected SendGeometryViewModel getSpecificViewModel() {
    return mViewModel;
  }

  private void initializeViewModel() {
    mViewModel =
        mSendGeometryViewModelFactory.create(mGGMapView, this::notifyInvalidInput, this::pickColor,
            this::pickBorderStyle, this::finish);
    mViewModel.init();
  }

  private void notifyInvalidInput() {
    Snackbar.make(getView(), getString(R.string.send_geometry_invalid_input_message),
        Snackbar.LENGTH_SHORT).show();
  }

  private void pickColor(int currentColor) {
    new SpectrumDialog.Builder(getContext()).setColors(R.array.icon_colors)
        .setSelectedColor(currentColor)
        .setDismissOnColorSelected(true)
        .setOnColorSelectedListener(
            (positiveResult, color) -> mViewModel.onColorSelected(positiveResult, color))
        .build()
        .show(getFragmentManager(), null);
  }

  private void pickBorderStyle(String currentStyle) {
    new AlertDialog.Builder(getContext()).setItems(R.array.border_styles,
        (dialog, which) -> mViewModel.onBorderStyleSelected(getBorderStyle(which))).create().show();
  }

  private String getBorderStyle(int which) {
    Configuration configuration = getEnglishConfiguration();

    return getContext().createConfigurationContext(configuration)
        .getResources()
        .getStringArray(R.array.border_styles)[which];
  }

  private Configuration getEnglishConfiguration() {
    Configuration configuration = new Configuration(getContext().getResources().getConfiguration());
    configuration.setLocale(new Locale("en"));
    return configuration;
  }

  private void setupMapClickDelegation() {
    mGGMapView.setOnMapGestureListener(new OnMapGestureListener() {
      @Override
      public void onLongPress(PointGeometry pointGeometry) {

      }

      @Override
      public void onTap(PointGeometry pointGeometry) {
        mViewModel.onLocationSelection(pointGeometry);
      }
    });
  }

  private FragmentSendGeometryBinding bind(View view) {
    FragmentSendGeometryBinding binding = FragmentSendGeometryBinding.bind(view);
    binding.setViewModel(mViewModel);
    return binding;
  }
}
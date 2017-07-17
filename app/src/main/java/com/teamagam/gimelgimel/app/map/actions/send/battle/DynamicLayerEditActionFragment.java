package com.teamagam.gimelgimel.app.map.actions.send.battle;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import butterknife.BindView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.databinding.FragmentDynamicLayerEditBinding;
import javax.inject.Inject;

public class DynamicLayerEditActionFragment
    extends BaseDrawActionFragment<DynamicLayerEditViewModel> {

  private static final int NO_OFFSET = 0;

  @Inject
  DynamicLayerEditViewModelFactory mDynamicLayerEditViewModelFactory;

  @BindView(R.id.dynamic_layer_edit_map)
  GGMapView mGGMapView;

  private DynamicLayerEditViewModel mDynamicLayerEditViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);
    mDynamicLayerEditViewModel = mDynamicLayerEditViewModelFactory.create(mGGMapView);

    FragmentDynamicLayerEditBinding binding = FragmentDynamicLayerEditBinding.bind(view);
    binding.setViewModel(mDynamicLayerEditViewModel);

    displayTwoFingersToast();

    return view;
  }

  @Override
  protected DynamicLayerEditViewModel getSpecificViewModel() {
    return mDynamicLayerEditViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.dynamic_layer_edit_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_dynamic_layer_edit;
  }

  private void displayTwoFingersToast() {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();

    View toastLayout = layoutInflater.inflate(R.layout.toast_two_fingers,
        (ViewGroup) getActivity().findViewById(R.id.toast_two_fingers_container));
    toastLayout.setBackgroundColor(
        ContextCompat.getColor(getContext(), R.color.semi_transparent_gray));

    showCustomToast(toastLayout);
  }

  private void showCustomToast(View toastLayout) {
    Toast t = new Toast(getActivity().getApplicationContext());
    t.setGravity(Gravity.CENTER_VERTICAL, NO_OFFSET, NO_OFFSET);
    t.setDuration(Toast.LENGTH_SHORT);
    t.setView(toastLayout);
    t.show();
  }
}

package com.teamagam.gimelgimel.app.map.actions.freedraw;

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
import com.teamagam.gimelgimel.app.map.actions.BaseStyleDrawActionFragment;
import com.teamagam.gimelgimel.databinding.FragmentFreeDrawBinding;
import javax.inject.Inject;

public class FreeDrawActionFragment extends BaseStyleDrawActionFragment<FreeDrawViewModel> {

  private static final int NO_OFFSET = 0;

  @Inject
  FreeDrawViewModelFactory mFreeDrawViewModelFactory;

  @BindView(R.id.free_draw_map)
  GGMapView mGGMapView;

  private FreeDrawViewModel mFreeDrawViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);
    mFreeDrawViewModel = mFreeDrawViewModelFactory.create(this::pickColor, mGGMapView);
    mFreeDrawViewModel.init();

    FragmentFreeDrawBinding binding = FragmentFreeDrawBinding.bind(view);
    binding.setViewModel(mFreeDrawViewModel);

    mGGMapView.setAllowPanning(false);

    displaySwitchToPanToast();

    return view;
  }

  @Override
  protected FreeDrawViewModel getSpecificViewModel() {
    return mFreeDrawViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.free_draw_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_free_draw;
  }

  private void displaySwitchToPanToast() {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    View toastLayout = getToastLayout(layoutInflater);
    showCustomToast(toastLayout);
  }

  private View getToastLayout(LayoutInflater layoutInflater) {
    View toastLayout = layoutInflater.inflate(R.layout.toast_switch_to_pan,
        (ViewGroup) getActivity().findViewById(R.id.toast_switch_to_pan_container));
    toastLayout.setBackgroundColor(
        ContextCompat.getColor(getContext(), R.color.semi_transparent_gray));
    return toastLayout;
  }

  private void showCustomToast(View toastLayout) {
    Toast t = new Toast(getActivity().getApplicationContext());
    t.setGravity(Gravity.CENTER_VERTICAL, NO_OFFSET, NO_OFFSET);
    t.setDuration(Toast.LENGTH_SHORT);
    t.setView(toastLayout);
    t.show();
  }
}

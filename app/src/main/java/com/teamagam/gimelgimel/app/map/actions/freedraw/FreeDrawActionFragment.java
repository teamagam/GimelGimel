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
import com.teamagam.gimelgimel.app.map.actions.BaseDrawActionFragment;
import com.teamagam.gimelgimel.databinding.FragmentFreeDrawBinding;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import javax.inject.Inject;

public class FreeDrawActionFragment extends BaseDrawActionFragment<FreedrawViewModel> {

  private static final int NO_OFFSET = 0;

  @Inject
  FreedrawViewModelFactory mFreedrawViewModelFactory;

  @BindView(R.id.free_draw_map)
  GGMapView mGGMapView;

  private FreedrawViewModel mFreedrawViewModel;

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {

    View view = super.onCreateView(inflater, container, savedInstanceState);
    mApp.getApplicationComponent().inject(this);
    mFreedrawViewModel = mFreedrawViewModelFactory.create(mGGMapView);
    mFreedrawViewModel.init();

    FragmentFreeDrawBinding binding = FragmentFreeDrawBinding.bind(view);
    binding.setViewModel(mFreedrawViewModel);

    mGGMapView.setAllowPanning(false);

    mGGMapView.getMapDragEventObservable()
        .doOnNext(mde -> sLogger.v("drag-event: "
            + mde.getFrom().getLongitude()
            + ","
            + mde.getFrom().getLatitude()
            + " - "
            + mde.getTo().getLongitude()
            + ","
            + mde.getTo().getLatitude()))
        .subscribe(new ErrorLoggingObserver<>());
    displayTwoFingersToast();

    return view;
  }

  @Override
  protected FreedrawViewModel getSpecificViewModel() {
    return mFreedrawViewModel;
  }

  @Override
  protected String getToolbarTitle() {
    return getString(R.string.free_draw_title);
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_free_draw;
  }

  private void displayTwoFingersToast() {
    LayoutInflater layoutInflater = getActivity().getLayoutInflater();
    View toastLayout = getToastLayout(layoutInflater);
    showCustomToast(toastLayout);
  }

  private View getToastLayout(LayoutInflater layoutInflater) {
    View toastLayout = layoutInflater.inflate(R.layout.toast_two_fingers,
        (ViewGroup) getActivity().findViewById(R.id.toast_two_fingers_container));
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

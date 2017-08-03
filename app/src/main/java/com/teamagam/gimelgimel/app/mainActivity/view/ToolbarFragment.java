package com.teamagam.gimelgimel.app.mainActivity.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.BaseFragment;
import com.teamagam.gimelgimel.app.mainActivity.viewmodel.ToolbarViewModel;
import javax.inject.Inject;

public class ToolbarFragment extends BaseFragment<GGApplication> {

  @BindView(R.id.main_toolbar)
  Toolbar mToolbar;

  @BindView(R.id.toolbar_boom_menu_button)
  BoomMenuButton mBoomMenuButton;

  @Inject
  ToolbarViewModel mToolbarViewModel;

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    ((MainActivity) getActivity()).getMainActivityComponent().inject(this);
    mToolbarViewModel.setView(this);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater,
      ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);

    ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

    initBoomMenu();

    return view;
  }

  @Override
  protected int getFragmentLayout() {
    return R.layout.fragment_toolbar;
  }

  private void initBoomMenu() {
    configureMenuButton();
    addButtons();
  }

  private void configureMenuButton() {
    mBoomMenuButton.setButtonEnum(ButtonEnum.Ham);
    mBoomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.HAM_5);
    mBoomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.Vertical);
  }

  private void addButtons() {
    addButton(R.drawable.ic_rectangle, R.color.themeYellow,
        R.string.menu_action_send_quadrilateral_title,
        i -> mToolbarViewModel.onSendPolygonClicked());
    addButton(R.drawable.ic_polygon_plus, R.color.themeRed,
        R.string.menu_action_draw_geometry_title, i -> mToolbarViewModel.onDrawGeometryClicked());
    addButton(R.drawable.ic_ruler, R.color.themeTeal, R.string.menu_action_measure_distance_title,
        i -> mToolbarViewModel.onMeasureDistanceClicked());
    addButton(R.drawable.ic_go_to_icon, R.color.themeBlue,
        R.string.menu_action_go_to_location_title, i -> mToolbarViewModel.onGoToLocationClicked());
    addButton(R.drawable.ic_gesture, R.color.themePink, R.string.menu_action_free_draw_title,
        i -> mToolbarViewModel.onFreeDrawClicked());
  }

  private void addButton(int imageRes,
      int colorRes,
      int textRes,
      OnBMClickListener onBMClickListener) {
    mBoomMenuButton.addBuilder(new HamButton.Builder().normalImageRes(imageRes)
        .normalColorRes(colorRes)
        .normalTextRes(textRes)
        .textSize((int) getResources().getDimension(R.dimen.font_size_small))
        .listener(onBMClickListener));
  }
}
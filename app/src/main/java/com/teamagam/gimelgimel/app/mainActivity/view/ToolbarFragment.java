package com.teamagam.gimelgimel.app.mainActivity.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import butterknife.BindView;

public class ToolbarFragment extends BaseFragment<GGApplication> {

    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.toolbar_boom_menu_button)
    BoomMenuButton mBoomMenuButton;

    private ToolbarViewModel mToolbarViewModel;

    public ToolbarFragment() {
        mToolbarViewModel = new ToolbarViewModel();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        mToolbar.inflateMenu(R.menu.main);

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
        mBoomMenuButton.setPiecePlaceEnum(PiecePlaceEnum.HAM_3);
        mBoomMenuButton.setButtonPlaceEnum(ButtonPlaceEnum.HAM_3);
    }

    private void addButtons() {
        addButton(R.drawable.ic_rectangle, R.color.orange,
                R.string.menu_action_send_quadrilateral_title,
                index -> mToolbarViewModel.onSendPolygonClicked());
        addButton(R.drawable.ic_polygon_plus, R.color.blue,
                R.string.menu_action_draw_geometry_title,
                index -> mToolbarViewModel.onDrawGeometryClicked());
        addButton(R.drawable.ic_ruler, R.color.green,
                R.string.menu_action_measure_distance_title,
                index -> mToolbarViewModel.onMeasureDistanceClicked());
    }

    private void addButton(int imageRes, int colorRes, int textRes,
                           OnBMClickListener onBMClickListener) {
        mBoomMenuButton.addBuilder(
                new HamButton.Builder()
                        .normalImageRes(imageRes)
                        .normalColorRes(colorRes)
                        .normalTextRes(textRes)
                        .textSize((int) getResources().getDimension(R.dimen.font_size_small))
                        .listener(onBMClickListener)
        );
    }
}
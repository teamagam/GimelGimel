package com.teamagam.gimelgimel.app.icons;

import android.app.Dialog;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.base.view.fragments.dialogs.BaseBindingDialogFragment;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class IconSelectionDialogFragment extends BaseBindingDialogFragment {

  private static final int COLUMNS_COUNT = 4;
  @BindView(R.id.dialog_icon_selection_grid)
  RecyclerView mIconsRecyclerView;
  @Inject
  IconProvider mIconProvider;
  @Inject
  IconSelectionViewModelFactory mIconSelectionViewModelFactory;
  private IconSelectionViewModel mIconSelectionViewModel;
  private OnIconSelectionListener mOnIconSelectedListener;

  public static void show(FragmentManager fragmentManager, OnIconSelectionListener listener) {
    IconSelectionDialogFragment fragment = new IconSelectionDialogFragment();
    fragment.setOnIconSelectedListener(listener);
    fragment.show(fragmentManager, IconSelectionDialogFragment.class.getSimpleName());
  }

  public void setOnIconSelectedListener(OnIconSelectionListener onIconSelectedListener) {
    mOnIconSelectedListener = onIconSelectedListener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);

    ((GGApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

    IconsAdapter iconsAdapter = new IconsAdapter();
    mIconSelectionViewModel =
        mIconSelectionViewModelFactory.create(mOnIconSelectedListener, iconsAdapter::addIcon);

    mIconsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), COLUMNS_COUNT));
    mIconsRecyclerView.setAdapter(iconsAdapter);
    dialog.setOnShowListener(dialogInterface -> mIconSelectionViewModel.start());

    return dialog;
  }

  @Override
  protected int getTitleResId() {
    return R.string.icon_selection_title;
  }

  @Override
  protected View onCreateDialogLayout() {
    return getActivity().getLayoutInflater().inflate(R.layout.dialog_icon_selection, null, false);
  }

  @Override
  protected int getDialogLayout() {
    return R.layout.dialog_icon_selection;
  }

  @Override
  protected boolean hasPositiveButton() {
    return true;
  }

  @Override
  protected String getPositiveString() {
    return getString(android.R.string.ok);
  }

  @Override
  protected void onPositiveClick() {
    super.onPositiveClick();
    mIconSelectionViewModel.onPositiveButtonClicked();
  }

  static class IconViewHolder extends RecyclerView.ViewHolder {

    private final IconProvider mIconProvider;

    @BindView(R.id.dialog_icon_grid_item_layout)
    ViewGroup mLayout;
    @BindView(R.id.dialog_icon_grid_item_image)
    ImageView mImage;
    @BindView(R.id.dialog_icon_grid_item_text)
    TextView mTextView;

    public IconViewHolder(View itemView, IconProvider iconProvider) {
      super(itemView);
      mIconProvider = iconProvider;
      ButterKnife.bind(this, itemView);
    }

    public void bind(Icon icon, View.OnClickListener onClickListener) {
      mTextView.setText(icon.getDisplayName());
      loadImageOffUi(icon);
      mLayout.setOnClickListener(onClickListener);
    }

    public void select() {
      setBackgroundColor(R.color.selected_list_item);
    }

    public void deselect() {
      setBackgroundColor(R.color.default_list_item);
    }

    private void loadImageOffUi(Icon icon) {
      Observable.just(icon)
          .observeOn(Schedulers.computation())
          .subscribe(new ErrorLoggingObserver<Icon>() {
            @Override
            public void onNext(Icon icon) {
              loadImage(icon);
            }
          });
    }

    private void loadImage(Icon icon) {
      Drawable iconDrawable =
          mIconProvider.getIconDrawable(icon.getId(), mImage.getMaxWidth(), mImage.getMaxHeight());

      mImage.post(() -> mImage.setImageDrawable(iconDrawable));
    }

    private void setBackgroundColor(int colorResId) {
      int color = ContextCompat.getColor(mLayout.getContext(), colorResId);
      mLayout.setBackgroundColor(color);
    }
  }

  private class IconsAdapter extends RecyclerView.Adapter<IconViewHolder> {

    private List<Icon> mIcons;
    private Icon mSelectedIcon;

    public IconsAdapter() {
      mIcons = new ArrayList<>();
    }

    public void addIcon(Icon icon) {
      mIcons.add(icon);
      notifyItemInserted(mIcons.size() - 1);
    }

    @Override
    public IconViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.dialog_icon_selection_item, parent, false);
      return new IconViewHolder(view, mIconProvider);
    }

    @Override
    public void onBindViewHolder(IconViewHolder holder, int position) {
      Icon icon = mIcons.get(position);
      holder.bind(icon, getIconClickListener(icon));
      setIconBackground(holder, icon);
    }

    @Override
    public int getItemCount() {
      return mIcons.size();
    }

    private View.OnClickListener getIconClickListener(Icon icon) {
      return view -> {
        mIconSelectionViewModel.onIconSelected(icon);
        mSelectedIcon = icon;
        notifyDataSetChanged();
      };
    }

    private void setIconBackground(IconViewHolder holder, Icon icon) {
      if (icon == mSelectedIcon) {
        holder.select();
      } else {
        holder.deselect();
      }
    }
  }
}

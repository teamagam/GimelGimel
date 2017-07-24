package com.teamagam.gimelgimel.app.map.view;

import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.viewModel.BaseMapViewModel;
import com.thebluealliance.spectrum.SpectrumDialog;
import java.util.Locale;

public abstract class DrawGeometryActionFragment<T extends BaseMapViewModel>
    extends BaseDrawActionFragment<T> {
  protected void pickColor(int currentColor) {
    new SpectrumDialog.Builder(getContext()).setColors(R.array.icon_colors)
        .setSelectedColor(currentColor)
        .setDismissOnColorSelected(true)
        .setOnColorSelectedListener(this::onColorSelected)
        .build()
        .show(getFragmentManager(), null);
  }

  protected void pickBorderStyle(String currentStyle) {
    new AlertDialog.Builder(getContext()).setItems(R.array.border_styles,
        (dialog, which) -> onBorderStyleSelected(getBorderStyle(which))).create().show();
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

  protected abstract void onBorderStyleSelected(String borderStyle);

  protected abstract void onColorSelected(boolean positiveResult, int color);
}

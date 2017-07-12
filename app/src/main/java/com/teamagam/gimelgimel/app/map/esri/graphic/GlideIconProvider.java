package com.teamagam.gimelgimel.app.map.esri.graphic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;

public class GlideIconProvider implements IconProvider {

  private static Logger sLogger = LoggerFactory.create(GlideIconProvider.class.getSimpleName());

  private Context mContext;
  private IconsRepository mIconsRepository;

  public GlideIconProvider(Context context, IconsRepository repository) {
    mContext = context;
    mIconsRepository = repository;
  }

  @Override
  public Drawable getIcon(String id) {
    Icon icon = mIconsRepository.get(id);

    try {
      return Glide.with(mContext).load(icon.getUrl()).into(-1, -1).get();
    } catch (Exception e) {
      sLogger.e(String.format("Could not create drawable from icon: %s, url: %s", icon.getId(),
          icon.getUrl()));

      return getDefaultIcon();
    }
  }

  private Drawable getDefaultIcon() {
    return ContextCompat.getDrawable(mContext, R.drawable.ic_map_marker);
  }
}

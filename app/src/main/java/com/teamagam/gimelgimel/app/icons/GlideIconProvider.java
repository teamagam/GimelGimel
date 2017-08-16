package com.teamagam.gimelgimel.app.icons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import javax.inject.Inject;

public class GlideIconProvider implements IconProvider {

  private static Logger sLogger = LoggerFactory.create(GlideIconProvider.class.getSimpleName());

  private Context mContext;
  private IconsRepository mIconsRepository;

  @Inject
  public GlideIconProvider(Context context, IconsRepository repository) {
    mContext = context;
    mIconsRepository = repository;
  }

  @Override
  public Drawable getIconDrawable(String iconId, int widthPx, int heightPx) {
    Icon icon = mIconsRepository.get(iconId);

    if (icon == null) {
      sLogger.w("Couldn't find an icon with id " + iconId);
      return getDefaultIcon();
    }

    return getDrawable(icon, widthPx, heightPx);
  }

  private Drawable getDefaultIcon() {
    return ContextCompat.getDrawable(mContext, R.drawable.ic_map_marker);
  }

  private Drawable getDrawable(Icon icon, int widthPx, int heightPx) {
    try {
      return Glide.with(mContext)
          .load(icon.getUrl())
          .diskCacheStrategy(DiskCacheStrategy.ALL)
          .into(widthPx, heightPx)
          .get();
    } catch (Exception e) {
      sLogger.e(String.format("Could not create drawable from icon: %s, url: %s", icon.getId(),
          icon.getUrl()), e);
      return getDefaultIcon();
    }
  }
}

package com.teamagam.gimelgimel.app.icons;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import java.net.URL;
import javax.inject.Inject;

public class GlideIconProvider implements IconProvider {

  private static final int DEFAULT_ICON_RES_ID = R.drawable.ic_map_marker;
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

  @Override
  public void load(ImageView imageView, String iconId) {
    Icon icon = mIconsRepository.get(iconId);

    if (icon == null) {
      loadDefault(imageView);
    } else {
      loadIcon(imageView, icon);
    }
  }

  private Drawable getDefaultIcon() {
    return ContextCompat.getDrawable(mContext, DEFAULT_ICON_RES_ID);
  }

  private Drawable getDrawable(Icon icon, int widthPx, int heightPx) {
    try {
      return baseGlideLoadRequest(icon).into(widthPx, heightPx).get();
    } catch (Exception e) {
      sLogger.e(String.format("Could not create drawable from icon: %s, url: %s", icon.getId(),
          icon.getUrl()), e);
      return getDefaultIcon();
    }
  }

  private DrawableRequestBuilder<URL> baseGlideLoadRequest(Icon icon) {
    return Glide.with(mContext).load(icon.getUrl()).diskCacheStrategy(DiskCacheStrategy.ALL);
  }

  private void loadDefault(ImageView imageView) {
    Glide.with(mContext).load(DEFAULT_ICON_RES_ID).into(imageView);
  }

  private void loadIcon(ImageView imageView, Icon icon) {
    try {
      baseGlideLoadRequest(icon).into(imageView);
    } catch (Exception e) {
      sLogger.e(
          String.format("Could not load drawable into view from icon: %s, url: %s", icon.getId(),
              icon.getUrl()), e);
      loadDefault(imageView);
    }
  }
}

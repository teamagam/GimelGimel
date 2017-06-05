package com.teamagam.gimelgimel.app.common.utils;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.teamagam.gimelgimel.R;
import java.net.UnknownHostException;

public class GlideLoader {

  private Context mContext;

  public GlideLoader(Context context) {
    mContext = context;
  }

  public void loadImage(Uri uri, ImageView imageView) {
    getBaseGlide(uri).into(imageView);
  }

  public void loadImage(Uri uri, ImageView imageView, View progressViewHolder) {
    getBaseGlide(uri).listener(new GlideListener(progressViewHolder)).into(imageView);
  }

  private DrawableRequestBuilder<Uri> getBaseGlide(Uri uri) {
    return Glide.with(mContext)
        .load(uri)
        .error(R.drawable.notification_error)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.ALL);
  }

  private class GlideListener implements RequestListener<Uri, GlideDrawable> {
    private View mPlaceHolder;

    GlideListener(View placeHolder) {
      mPlaceHolder = placeHolder;
    }

    @Override
    public boolean onException(Exception e,
        Uri model,
        Target<GlideDrawable> target,
        boolean isFirstResource) {
      if (!(e instanceof UnknownHostException)) {
        hidePlaceHolder();
      }

      return false;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource,
        Uri model,
        Target<GlideDrawable> target,
        boolean isFromMemoryCache,
        boolean isFirstResource) {
      hidePlaceHolder();
      return false;
    }

    private void hidePlaceHolder() {
      mPlaceHolder.setVisibility(View.GONE);
    }
  }
}

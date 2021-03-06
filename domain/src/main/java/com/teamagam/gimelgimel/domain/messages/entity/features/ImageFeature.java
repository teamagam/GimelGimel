package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;

public class ImageFeature implements MessageFeatureVisitable {

  private long mTime;
  private String mSource;
  private String mRemoteUrl;
  private String mLocalUrl;

  public ImageFeature(long time, String source, String remoteUrl, String localUrl) {
    mTime = time;
    mSource = source;
    mRemoteUrl = remoteUrl;
    mLocalUrl = localUrl;
  }

  public long getTime() {
    return mTime;
  }

  public String getSource() {
    return mSource;
  }

  public String getRemoteUrl() {
    return mRemoteUrl;
  }

  public String getLocalUrl() {
    return mLocalUrl;
  }

  @Override
  public void accept(MessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}

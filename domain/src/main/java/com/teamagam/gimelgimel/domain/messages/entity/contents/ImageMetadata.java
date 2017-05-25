package com.teamagam.gimelgimel.domain.messages.entity.contents;

import java.util.Date;

public class ImageMetadata {
  protected long mTime;
  protected String mSource;
  protected String mRemoteUrl;
  protected String mLocalUrl;

  public ImageMetadata(long time, String remoteUrl, String localUrl, String source) {
    mSource = source;
    mRemoteUrl = remoteUrl;
    mTime = time;
    mLocalUrl = localUrl;
  }

  public long getTime() {
    return mTime;
  }

  public String getSource() {
    return mSource;
  }

  public String getLocalUrl() {
    return mLocalUrl;
  }

  public String getRemoteUrl() {
    return mRemoteUrl;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("ImageMetadata[");
    s.append(mSource);
    s.append(": ");
    if (mTime == 0) {
      s.append(" t=?!?");
    } else {
      s.append(" t=");
      s.append(new Date(mTime));
    }

    if (mRemoteUrl != null) {
      s.append(" url=");
      s.append(mRemoteUrl);
    }

    s.append(']');
    return s.toString();
  }
}

package com.teamagam.gimelgimel.domain.layers.entitiy;

import java.net.URI;

public class VectorLayerPresentation extends VectorLayer {

  private URI mLocalURI;
  private boolean mIsShown;

  protected VectorLayerPresentation(VectorLayer vectorLayer, URI localURI, boolean isShown) {
    super(vectorLayer.getId(), vectorLayer.getName(), vectorLayer.getUrl(),
        vectorLayer.getSeverity(), vectorLayer.getCategory(), vectorLayer.getVersion());
    mLocalURI = localURI;
    mIsShown = isShown;
  }

  public static VectorLayerPresentation createShown(VectorLayer vectorLayerContent,
      URI localURI) {
    return create(vectorLayerContent, localURI, true);
  }

  public static VectorLayerPresentation createHidden(VectorLayer vectorLayerContent,
      URI localURI) {
    return create(vectorLayerContent, localURI, false);
  }

  private static VectorLayerPresentation create(VectorLayer vectorLayerContent,
      URI localURI,
      boolean isShown) {
    return new VectorLayerPresentation(vectorLayerContent, localURI, isShown);
  }

  public URI getLocalURI() {
    return mLocalURI;
  }

  public boolean isShown() {
    return mIsShown;
  }
}

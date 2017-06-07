package com.teamagam.gimelgimel.domain.layers.entitiy;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayerContent;
import java.net.URI;

public class VectorLayerContentPresentation extends VectorLayerContent {

  private URI mLocalURI;
  private boolean mIsShown;

  private VectorLayerContentPresentation(VectorLayerContent vectorLayerContent, URI localURI, boolean isShown) {
    super(vectorLayerContent.getId(), vectorLayerContent.getName(), vectorLayerContent.getVersion(),
        vectorLayerContent.getSeverity(), vectorLayerContent.getCategory());
    mLocalURI = localURI;
    mIsShown = isShown;
  }

  public static VectorLayerContentPresentation createShown(VectorLayerContent vectorLayerContent, URI localURI) {
    return create(vectorLayerContent, localURI, true);
  }

  public static VectorLayerContentPresentation createHidden(VectorLayerContent vectorLayerContent, URI localURI) {
    return create(vectorLayerContent, localURI, false);
  }

  private static VectorLayerContentPresentation create(VectorLayerContent vectorLayerContent,
      URI localURI,
      boolean isShown) {
    return new VectorLayerContentPresentation(vectorLayerContent, localURI, isShown);
  }

  public URI getLocalURI() {
    return mLocalURI;
  }

  public boolean isShown() {
    return mIsShown;
  }
}

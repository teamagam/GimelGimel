package com.teamagam.gimelgimel.app.mainActivity.drawer.layers;

import com.teamagam.gimelgimel.app.mainActivity.drawer.LayersNodeDisplayer;
import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import java.util.HashMap;
import java.util.Map;

public abstract class NodeSelectionDisplayer<T extends IdentifiedData> {

  private final LayersNodeDisplayer mLayersNodeDisplayer;
  private final Map<String, String> mDisplayedEntitiesToNodeIdsMap;

  public NodeSelectionDisplayer(LayersNodeDisplayer layersNodeDisplayer) {
    mLayersNodeDisplayer = layersNodeDisplayer;
    mDisplayedEntitiesToNodeIdsMap = new HashMap<>();
  }

  public synchronized void display(T item, LayersNodeDisplayer.Node parent) {
    displayNode(item, parent);
    setSelectedState(item);
  }

  protected abstract LayersNodeDisplayer.Node createNode(T item,
      LayersNodeDisplayer.Node parentNode);

  protected abstract boolean isSelected(T item);

  private void displayNode(T item, LayersNodeDisplayer.Node parent) {
    if (!alreadyExists(item)) {
      LayersNodeDisplayer.Node node = createNode(item, parent);
      mLayersNodeDisplayer.addNode(node);
      mDisplayedEntitiesToNodeIdsMap.put(item.getId(), node.getId());
    }
  }

  private boolean alreadyExists(T item) {
    return mDisplayedEntitiesToNodeIdsMap.containsKey(item.getId());
  }

  private void setSelectedState(T item) {
    mLayersNodeDisplayer.setNodeSelectionState(mDisplayedEntitiesToNodeIdsMap.get(item.getId()),
        isSelected(item));
  }
}

package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import android.support.v7.app.AlertDialog;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteRemoveDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import java.util.List;

public class DynamicLayerEntityDeleteListener implements MapEntityClickedListener {

  private final DeleteEntityDialogDisplayer mDeleteEntityDialogDisplayer;
  private final SendRemoteRemoveDynamicEntityRequestInteractorFactory
      mRemoveDynamicEntityRequestInteractorFactory;
  private final DisplayDynamicLayersInteractorFactory mDisplayDynamicLayersInteractorFactory;
  private final String mDynamicLayerId;

  private List<GeoEntity> mDynamicLayerEntities;
  private DisplayDynamicLayersInteractor mDisplayDynamicLayersInteractor;

  public DynamicLayerEntityDeleteListener(DeleteEntityDialogDisplayer deleteEntityDialogDisplayer,
      SendRemoteRemoveDynamicEntityRequestInteractorFactory removeDynamicEntityRequestInteractorFactory,
      DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      String dynamicLayerId) {
    mDeleteEntityDialogDisplayer = deleteEntityDialogDisplayer;
    mRemoveDynamicEntityRequestInteractorFactory = removeDynamicEntityRequestInteractorFactory;
    mDisplayDynamicLayersInteractorFactory = displayDynamicLayersInteractorFactory;
    mDynamicLayerId = dynamicLayerId;
  }

  public void startDynamicLayerEntitiesSync() {
    mDisplayDynamicLayersInteractor = mDisplayDynamicLayersInteractorFactory.create(dl -> {
      if (isCurrentDynamicLayer(dl)) {
        mDynamicLayerEntities = dl.getEntities();
      }
    });
    mDisplayDynamicLayersInteractor.execute();
  }

  public void stopDynamicLayerEntitiesSync() {
    if (mDisplayDynamicLayersInteractor != null) {
      mDisplayDynamicLayersInteractor.unsubscribe();
    }
  }

  @Override
  public void entityClicked(String entityId) {
    if (isFromCurrentDynamicLayer(entityId)) {
      mDeleteEntityDialogDisplayer.display((dialogInterface, i) -> deleteEntity(entityId));
    }
  }

  @Override
  public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {

  }

  private boolean isCurrentDynamicLayer(DynamicLayerPresentation dl) {
    return dl.getId().equals(mDynamicLayerId);
  }

  private boolean isFromCurrentDynamicLayer(String entityId) {
    List<GeoEntity> entities = mDynamicLayerEntities;
    for (GeoEntity entity : entities) {
      if (entity.getId().equals(entityId)) {
        return true;
      }
    }
    return false;
  }

  private void deleteEntity(String entityId) {
    mRemoveDynamicEntityRequestInteractorFactory.create(mDynamicLayerId, entityId).execute();
  }

  public interface DeleteEntityDialogDisplayer {
    void display(AlertDialog.OnClickListener listener);
  }
}

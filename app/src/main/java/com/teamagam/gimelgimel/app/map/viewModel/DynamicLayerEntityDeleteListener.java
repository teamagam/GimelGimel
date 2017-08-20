package com.teamagam.gimelgimel.app.map.viewModel;

import android.support.v7.app.AlertDialog;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteRemoveDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import java.util.List;

class DynamicLayerEntityDeleteListener implements MapEntityClickedListener {

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
      if (dl.getId().equals(mDynamicLayerId)) {
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
    if (isBelongToCurrentDynamicLayer(entityId)) {
      mDeleteEntityDialogDisplayer.display(
          (dialogInterface, i) -> mRemoveDynamicEntityRequestInteractorFactory.create(
              mDynamicLayerId, entityId).execute());
    }
  }

  @Override
  public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {

  }

  private boolean isBelongToCurrentDynamicLayer(String entityId) {
    List<GeoEntity> entities = mDynamicLayerEntities;
    for (GeoEntity entity : entities) {
      if (entity.getId().equals(entityId)) {
        return true;
      }
    }
    return false;
  }

  public interface DeleteEntityDialogDisplayer {
    void display(AlertDialog.OnClickListener listener);
  }
}

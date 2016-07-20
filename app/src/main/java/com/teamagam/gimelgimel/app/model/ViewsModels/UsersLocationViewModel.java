package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.view.fragments.ViewerFragment;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds other users' last location
 * Synchronizes VectorLayer with its internal state
 */
public class UsersLocationViewModel {

    private HashMap<String, MessageUserLocation> mUserIdToUserLocation;
    private ViewerFragment.MessageSymbolizer mSymbolizer;

    public UsersLocationViewModel(ViewerFragment.MessageSymbolizer symbolizer) {
        mUserIdToUserLocation = new HashMap<>();
        mSymbolizer = symbolizer;
    }

    public void save(MessageUserLocation message) {
        mUserIdToUserLocation.put(message.getSenderId(), message);
    }

    public void synchronizeToVectorLayer(VectorLayer vectorLayer) {
        for (Map.Entry<String, MessageUserLocation> kvp : mUserIdToUserLocation.entrySet()) {
            synchronizeToVectorLayer(vectorLayer, kvp.getValue());
        }
    }

    private void synchronizeToVectorLayer(VectorLayer vectorLayer,
                                          MessageUserLocation userLocation) {
        if (isUserEntityExists(vectorLayer, userLocation.getSenderId())) {
            updateExistingUserLocation(userLocation, vectorLayer.getEntity(userLocation.getSenderId()));
        } else {
            addNewUserLocation(vectorLayer, userLocation);
        }
    }

    private boolean isUserEntityExists(VectorLayer vectorLayer, String id) {
        return vectorLayer.getEntity(id) != null;
    }

    private void updateExistingUserLocation(MessageUserLocation userLocation, Entity userEntity) {
        userEntity.updateSymbol(mSymbolizer.symbolize(userLocation));
        userEntity.updateGeometry(userLocation.getContent().getLocation());
    }

    private void addNewUserLocation(VectorLayer vectorLayer,
                                    MessageUserLocation userLocation) {
        Entity newEntity = createUserEntity(userLocation);
        vectorLayer.addEntity(newEntity);
    }

    private Entity createUserEntity(MessageUserLocation userLocation) {
        return new Point.Builder()
                .setId(userLocation.getSenderId())
                .setGeometry(userLocation.getContent().getLocation())
                .setSymbol(mSymbolizer.symbolize(userLocation))
                .build();
    }

}

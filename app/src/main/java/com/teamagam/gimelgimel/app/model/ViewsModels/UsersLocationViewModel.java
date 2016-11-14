package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.symbols.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.message.model.MessageUserLocationApp;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Holds other users' last location
 * Synchronizes VectorLayer with its internal state
 */
@PerActivity
public class UsersLocationViewModel {

    private HashMap<String, MessageUserLocationApp> mUserIdToUserLocation;

    @Inject
    @Named("entitySymbolizer")
    IMessageSymbolizer mSymbolizer;

    @Inject
    public UsersLocationViewModel() {
        mUserIdToUserLocation = new HashMap<>();
    }

    public void save(MessageUserLocationApp message) {
        mUserIdToUserLocation.put(message.getSenderId(), message);
    }

//    public void synchronizeToVectorLayer(VectorLayer vectorLayer) {
//        for (Map.Entry<String, MessageUserLocationApp> kvp : mUserIdToUserLocation.entrySet()) {
//            synchronizeToVectorLayer(vectorLayer, kvp.getValue());
//        }
//    }

//    }
//
//    private boolean isUserEntityExists(VectorLayer vectorLayer, String id) {
//        return vectorLayer.getEntity(id) != null;
//    }
//
//    private void updateExistingUserLocation(MessageUserLocationApp userLocation, Entity userEntity) {
//        userEntity.updateSymbol(mSymbolizer.symbolize(userLocation));
//        userEntity.updateGeometry(userLocation.getContent().getLocation());
//    }
//
//    private void addNewUserLocation(VectorLayer vectorLayer,
//                                    MessageUserLocationApp userLocation) {
//        Entity newEntity = createUserEntity(userLocation);
//        vectorLayer.addEntity(newEntity);
//    }
//
//    private Entity createUserEntity(MessageUserLocationApp userLocation) {
//        return new Point.Builder()
//                .setId(userLocation.getSenderId())
//                .setGeometry(userLocation.getContent().getLocation())
//                .setSymbol(mSymbolizer.symbolize(userLocation))
//                .build();
//    }

}

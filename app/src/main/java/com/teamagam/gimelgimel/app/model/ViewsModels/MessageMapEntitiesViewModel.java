package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.app.model.entities.UserLocation;
import com.teamagam.gimelgimel.app.model.entities.messages.SelectedMessageModel;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Point;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.PointImageSymbol;
import com.teamagam.gimelgimel.app.view.viewer.data.symbols.Symbol;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds other users' last location
 * Synchronizes VectorLayer with its internal state
 */
public class MessageMapEntitiesViewModel implements Entity.OnClickListener {

    private final SelectedMessageModel mSelectedModel;
    private Map<Entity, Message> mEntityToMessageHashMap;
//    private UserLocationSymbolizer mUserLocationSymbolizer;

    public MessageMapEntitiesViewModel(SelectedMessageModel model) {
        mEntityToMessageHashMap = new HashMap<>();
        mSelectedModel = model;
    }

    private Entity createUserEntity(PointGeometry pointGeometry) {

        //Todo: use symbol interface
        PointImageSymbol pointSymbol = new PointImageSymbol(
                "Cesium/Assets/Textures/maki/marker.png", 36,
                36);
        return new Point.Builder()
                .setGeometry(pointGeometry)
                .setSymbol(pointSymbol)
                .build();
    }

    @Override
    public void onEntityClick(Entity entity) {
        mSelectedModel.select(mEntityToMessageHashMap.get(entity));
    }

    public void addMessage(PointGeometry pointGeometry) {
    }

//    public void save(UserLocation userLocation) {
//        mUserIdToUserLocation.put(userLocation.getId(), userLocation);
//    }
//
//    public void synchronizeToVectorLayer(VectorLayer vectorLayer) {
//        for (Map.Entry<String, UserLocation> kvp : mUserIdToUserLocation.entrySet()) {
//            synchronizeToVectorLayer(vectorLayer, kvp.getValue());
//        }
//    }
//
//    private void synchronizeToVectorLayer(VectorLayer vectorLayer, UserLocation userLocation) {
//        if (isUserEntityExists(vectorLayer, userLocation.getId())) {
//            updateExistingUserLocation(userLocation, vectorLayer.getEntity(userLocation.getId()));
//        } else {
//            addNewUserLocation(vectorLayer, userLocation);
//        }
//    }
//
//    private boolean isUserEntityExists(VectorLayer vectorLayer, String id) {
//        return vectorLayer.getEntity(id) != null;
//    }
//
//    private void updateExistingUserLocation(UserLocation userLocation, Entity userEntity) {
//        userEntity.updateSymbol(mUserLocationSymbolizer.symbolize(userLocation));
//        userEntity.updateGeometry(userLocation.getLocationSample().getLocation());
//    }
//
//    private void addNewUserLocation(VectorLayer vectorLayer,
//                                    UserLocation userLocation) {
//        Entity newEntity = createUserEntity(userLocation);
//        vectorLayer.addEntity(newEntity);
//    }
//
//    public interface UserLocationSymbolizer {
//        Symbol symbolize(UserLocation userLocation);
//    }
}

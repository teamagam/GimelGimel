/**
 * Defines and wraps available functionality injected via
 * JavascriptInterfaces into the containing WebView
 */

GG.AndroidAPI = {

    /**
     * Updates android via injected JavascriptInterface
     *
     * @param location - location to push into Android for update.
     * Should be an object with latitude, longitude properties
     */
    updateViewedLocation: function (location) {
        var locationJsonString = JSON.stringify(location);
//        CesiumMapGestureDetector
        CesiumMapGestureDetector.updateViewedLocation(locationJsonString);
    },

    /**
     * Updated android via injected JavascriptInterface
     *
     * @param location - location to push into android for update.
     * Should be an object with latitude, longitude and altitude
     */
    onLongPress: function (location) {
        var locationJsonString = JSON.stringify(location);
        CesiumMapGestureDetector.onLongPress(locationJsonString)
    },

    /**
     * Updated android via injected JavascriptInterface
     *
     * @param location - location to push into android for update.
     * Should be an object with latitude, longitude and altitude
     */
    onDoubleTap: function (location) {
        var locationJsonString = JSON.stringify(location);
        CesiumMapGestureDetector.onDoubleTap(locationJsonString)
    },

    onEntityClicked: function(layerId, entityId){
        CesiumEntityClickListener.OnEntityClicked(layerId, entityId)
    },

};


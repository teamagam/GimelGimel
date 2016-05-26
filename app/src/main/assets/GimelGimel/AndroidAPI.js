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
    updateLocation: function (location) {
        var locationJsonString = JSON.stringify(location);
        LocationUpdater.UpdateSelectedLocation(locationJsonString);
    },
    /**
     * Updates android via injected JavascriptInterface that Cesium viewer is now ready (finished loading)
     */
    onReady: function () {
        CesiumReady.onReady();
    }
};


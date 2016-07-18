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
    updateSelectedLocation: function (location) {
        var locationJsonString = JSON.stringify(location);
        LocationUpdater.UpdateSelectedLocation(locationJsonString);
    },
    /**
     * Updated android via injected JavascriptInterface
     *
     * @param location - location to push into android for update.
     * Should be an object with latitude, longitude and altitude
     */
    updateViewedLocation: function (location) {
        var locationJsonString = JSON.stringify(location);
        LocationUpdater.UpdateViewedLocation(locationJsonString)
    },

};


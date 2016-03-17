/**
 * Created by Bar on 01-Mar-16.
 */

var GG = GG || {};

GG.Utils = {
    /***
     * Converts an array of objects with latitude and longitude properties
     * to a degrees-array compatible with Cesium format for polyline/polygon
     * creation
     *
     * @param locations - an array of objects, each with latitude and longitude (Numbers) properties
     * @returns {Array} - Cesium-formatted array for
     */
    locationsToDegreesArray: function (locations) {
        var degArray = [];
        for (var i = 0; i < locations.length; i++) {
            degArray[i * 2] = locations[i].longitude;
            degArray[i * 2 + 1] = locations[i].latitude;
        }
        return degArray;
    },

    assertIdExists: function (id, collection) {
        //if (typeof collection[id] === "undefined") {
        if(!collection[id]){
            throw new Error("An entity with given id (" + id + ") doesn't exist");
        }
    },

    assertIdNotExists: function (id, collection) {
        if (collection[id]) {
            throw new Error("An entity with given id (" + id + ") already exist");
        }
    },
    pinBuilder: function () {
        return new Cesium.PinBuilder();
    },
    /***
     * Asserts given object is defined. Otherwise, throws an error.
     *
     * @param obj - Object to assert
     * @param argName -  Object name to be used in error message
     */
    assertDefined: function (obj, argName) {
        if (typeof obj === "undefined") {
            argName = argName || "obj";
            var message = argName + " is undefined";
            throw new Error(message);
        }
    },
    /**
     * Sets an action to be executed when event of given type occurs in viewer
     *
     * @param {Viewer} viewer - cesium viewer to attached the listener to
     * @param {Number} screenSpaceEventType - the type of space event to register action.
     * Must be one of Cesium.ScreenSpaceEventType types.
     * @param {function(movement)} listener - function to be called when event occurs
     */
    setScreenSpaceEventAction: function(viewer, screenSpaceEventType, action){
        var handler = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas);
        handler.setInputAction(action, screenSpaceEventType);
    }
};
